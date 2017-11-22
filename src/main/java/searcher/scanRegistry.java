package searcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

/**
 * For every image, check if there are changes on its layers
 * @author Simone Erba

 */
public class scanRegistry extends Thread {
	Users u;
	Transaction t;
	LoggerUpdater l;
	static GraphOperations g;
	GraphDatabaseService graph;

	public scanRegistry(Users u) {
		super();
		this.u = u;
		g = GraphOperations.getInstance();

	}

	@Override
	/**
	 * Search images by user
	 */
	public void run() {

		while (true) {
			graph = g.getGraph();
			t = g.getGraph().beginTx();
			ConcurrentLinkedQueue<String> users = u.getC();
			Iterator<String> i = users.iterator();
			int k = 0;
			while (i.hasNext()) {
				if (k % 1000 == 0) {
					t.success();
					t.close();
					t = g.getGraph().beginTx();
				}

				k++;
				String user = i.next();
				l.getInstance().write("updating user: " + user + " number " + k);
				Index<Node> index = graph.index().forNodes("indexUser");
				IndexHits<Node> n = index.get("user", user);
				ResourceIterator<Node> ind = n.iterator();
				while (ind.hasNext()) {
					Node n2 = ind.next();
					checkUpdates(n2);
				}
			}
			t.success();
			t.close();
		}
	}

	/**
	 * check if the are updates on the layers
	 * 
	 * @param n
	 */
	private void checkUpdates(Node n) {
		// TODO Auto-generated method stub
		l.getInstance().write("checking updates for " + n.getProperty("fulltag"));
		String s = (String) n.getProperty("layers");
		List<String> list = Arrays.asList(s.toString().split("\\s*,\\s*"));
		Iterator<String> it = list.iterator();
		int count = 0;
		while (it.hasNext()) {
			String a = it.next();
			if (count == 0) {
				int index = a.length();
				a = a.substring(1, index);
				list.set(0, a);
			}
			if (count == list.size() - 1) {
				int index = a.length();
				a = a.substring(0, index - 1);
				list.set(count, a);
			}
			count++;
		}
		String url = "https://registry-1.docker.io/v2/" + n.getProperty("fullname") + "/manifests/"
				+ n.getProperty("tag");
		URL obj = null;

		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// optional default is GET
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (responseCode == 200) {

			String st = "";
			try {
				st = con.getResponseMessage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// System.out.println("Response Code : " + responseCode3);
			if (responseCode == 200) {

				InputStream in = null;
				try {
					in = con.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String encoding = con.getContentEncoding();
				encoding = encoding == null ? "UTF-8" : encoding;
				String body = null;
				try {
					body = IOUtils.toString(in, encoding);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject json = new JSONObject(body);
				String token = json.getString("token");
				String url3 = "https://registry-1.docker.io/v2/" + n.getProperty("fullname") + "/manifests/"
						+ n.getProperty("tag");
				URL obj3 = null;

				try {
					obj3 = new URL(url3);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				HttpURLConnection con3 = null;
				try {
					con3 = (HttpURLConnection) obj3.openConnection();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				// optional default is GET
				try {
					con3.setRequestMethod("GET");
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// add request header
				con3.setRequestProperty("Authorization", "Bearer " + token);
				try {
					String s3 = con3.getResponseMessage();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int responseCode3 = 0;
				try {
					responseCode3 = con3.getResponseCode();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (responseCode3 == 200) {

					InputStream in3 = null;
					try {
						in3 = con3.getInputStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String encoding3 = con3.getContentEncoding();
					encoding3 = encoding3 == null ? "UTF-8" : encoding3;
					String body3 = null;
					try {
						body3 = IOUtils.toString(in3, encoding3);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JSONObject json2 = new JSONObject(body3);
					JSONArray j = json2.getJSONArray("fsLayers");
					JSONArray history = json2.getJSONArray("history");
					String[] v = list.toArray(new String[0]);
					String[] v2 = new String[j.length()];
					for (int i = j.length() - 1; i >= 0; i--) {
						v2[i] = j.getJSONObject(i).getString("blobSum");
					}
					if (v.length != v2.length) {
						l.getInstance().write("updated " + n.getProperty("fulltag"));
						g.delete(n);
						g.insertSingle(n.getProperty("name").toString(), n.getProperty("repo").toString(),
								n.getProperty("tag").toString(), n.getProperty("date").toString(), j, history);
					} else {
						boolean mod = false;
						for (int i = 0; i < v.length; i++) {
							if (!v[i].equals(v2[i])) {
								mod = true;
								l.getInstance().write("updated " + n.getProperty("fulltag"));
								g.delete(n);
								g.insertSingle(n.getProperty("name").toString(), n.getProperty("repo").toString(),
										n.getProperty("tag").toString(), n.getProperty("date").toString(), j, history);
							}
						}
						if (mod == false) {
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							Date date = new Date();
							String now = dateFormat.format(date);
							n.setProperty("date", now);
						}
					}
				}
			}
		}
	}
}
