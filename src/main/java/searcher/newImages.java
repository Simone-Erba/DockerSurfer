package searcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
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
 *  A class to find new images, checking the image for every user
 * @author Simone-Erba
 *
 */
public class newImages extends Thread {

	GraphOperations g;
	LoggerUpdater l;
	Transaction t;

	Users u;

	public newImages(Users u) {
		this.u = u;
	}

	public void run() {
		g = GraphOperations.getInstance();
		t = g.getGraph().beginTx();
		while (true) {
			searchv1();
		}

	}

	/**
	 * For every user, get his images and see if there are new images
	 */
	private void searchv1() {

		int jjj = 0;
		while (true) {
			ConcurrentLinkedQueue<String> users = u.getC();
			Iterator<String> i = users.iterator();
			GraphDatabaseService graph = g.getGraph();

			while (i.hasNext()) {
				jjj++;
				String user = i.next();
				l.getInstance().write("new images for user " + user + " number: " + jjj);

				int n = 0;
				int numpages = 0;
				do {
					String url2 = "https://index.docker.io/v1/search?q=" + user + "&page=" + n + "&n=100";
					l.getInstance().write("page " + n + " of " + user);
					URL obj2 = null;
					try {
						obj2 = new URL(url2);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					HttpURLConnection con2 = null;
					try {
						con2 = (HttpURLConnection) obj2.openConnection();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// optional default is GET
					try {
						con2.setRequestMethod("GET");
					} catch (ProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					int responseCode2 = 0;
					try {
						responseCode2 = con2.getResponseCode();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String s2 = null;
					try {
						s2 = con2.getResponseMessage();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					InputStream in2 = null;
					try {
						in2 = con2.getInputStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String encoding2 = con2.getContentEncoding();
					encoding2 = encoding2 == null ? "UTF-8" : encoding2;
					String body2 = null;
					try {
						body2 = IOUtils.toString(in2, encoding2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					JSONObject json = new JSONObject(body2);
					numpages = json.getInt("num_pages");
					JSONArray array = json.getJSONArray("results");
					for (int j = 0; j < array.length(); j++) {
						String name = array.getJSONObject(j).getString("name");
						String user2 = name.substring(0, name.lastIndexOf("/"));
						String repo2 = name.substring(name.lastIndexOf("/") + 1, name.length());
						int k = 0;
						Index<Node> index = graph.index().forNodes("indexRepo");
						IndexHits<Node> in = index.get("repo", name);
						ResourceIterator<Node> ind = in.iterator();
						if (!ind.hasNext()) {
							l.getInstance().write("new image " + name + " from already existing user: " + user);
							newImage(name);
						}
					}

				} while (n <= numpages);
				t.success();// commit ad ogni utente
				t.close();
				t = g.getGraph().beginTx();
			}

		}
	}

	/**
	 * Download a new image
	 * 
	 * @param name
	 */
	private void newImage(String name) {

		String url2 = "https://auth.docker.io/token?service=registry.docker.io&scope=repository:" + name + ":pull";
		URL obj2 = null;

		try {
			obj2 = new URL(url2);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpURLConnection con2 = null;
		try {
			con2 = (HttpURLConnection) obj2.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// optional default is GET
		try {
			con2.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String s2 = con2.getResponseMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream in2 = null;
		try {
			in2 = con2.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String encoding2 = con2.getContentEncoding();
		encoding2 = encoding2 == null ? "UTF-8" : encoding2;
		String body2 = null;
		try {
			body2 = IOUtils.toString(in2, encoding2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(body2);
		int responseCode2 = 0;
		try {
			responseCode2 = con2.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (responseCode2 == 200) {
			JSONObject json = new JSONObject(body2);
			String token = json.getString("token");

			String url4 = "https://registry-1.docker.io/v2/" + name + "/tags/list";
			URL obj4 = null;

			try {
				obj4 = new URL(url4);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HttpURLConnection con4 = null;
			try {
				con4 = (HttpURLConnection) obj4.openConnection();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			// optional default is GET
			try {
				con4.setRequestMethod("GET");
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con4.setRequestProperty("Authorization", "Bearer " + token);

			try {
				String s4 = con4.getResponseMessage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int responseCode4 = 0;
			try {
				responseCode4 = con4.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (responseCode4 == 200) {

				InputStream in4 = null;
				try {
					in4 = con4.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String encoding4 = con4.getContentEncoding();
				encoding4 = encoding4 == null ? "UTF-8" : encoding4;
				String body4 = null;
				try {
					body4 = IOUtils.toString(in4, encoding4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject json4 = new JSONObject(body4);
				JSONArray tags = json4.getJSONArray("tags");
				for (int i = 0; i < tags.length(); i++) {
					String tag = tags.getString(i);
					System.out.println(i + "          " + tag);
					pullTag(name, tag, token);
				}
			}
		}

	}

	/**
	 * Pull a single tag and insert it in the database
	 * 
	 * @param nome
	 * @param tag
	 * @param token
	 *            token used for authentication
	 */
	private void pullTag(String nome, String tag, String token) {
		// TODO Auto-generated method stub
		// System.out.println(token);
		String url3 = "https://registry-1.docker.io/v2/" + nome + "/manifests/" + tag;
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
		// con.setRequestProperty("username", "simoneerba");
		con3.setRequestProperty("Authorization", "Bearer " + token);
		// System.out.println("content " +
		// con3.getHeaderField("Www-Authenticate"));

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
		// System.out.println("Response Code : " + responseCode3);
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
			// body3=body3.substring(1,body3.length()-1);
			// System.out.println(body3);
			// body3="["+body3+"]";
			JSONObject json2 = new JSONObject(body3);
			// op.insert(json2);

			pullHttpFile(json2, tag);

		}
	}

	/**
	 * Read the json answer
	 * 
	 * @param json2
	 * @param tag
	 */
	private void pullHttpFile(JSONObject json2, String tag) {
		String repo = json2.getString("name");
		int index = repo.lastIndexOf("/");
		if (index == -1) {
			repo = "library/" + repo;
			repo.lastIndexOf("/");
		}
		String name = repo.substring(0, index);
		String surname = repo.substring(index + 1, repo.length());
		JSONArray layers2 = json2.getJSONArray("fsLayers");
		JSONArray history = json2.getJSONArray("history");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String s = dateFormat.format(date); // 2016/11/16 12:08:4

		g.insertSingle(name, surname, tag, s, layers2, history);
	}
}
