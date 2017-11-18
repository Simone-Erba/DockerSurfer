package searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.graphdb.Transaction;

public class NamespacePuller extends Thread{
	/**
	 * a file that contains the 1000 most used english words, used for querying the Docker Registry
	 */
	private File few;
	Users u;
	LoggerUpdater l;
	public NamespacePuller(Users u) {
		super();
		this.u = u;
	}
	@Override
	public void run()
	{
		//SHOULD PUT THIS PATH IN CONFIG.PROPERTIES FILE
		few=new File("/home/ec2-user/files/words.txt");
		while(true)
		{
		searchv1();

		}
	}
	
	private void searchv1() {

			//per ogni parola
			FileReader fr = null;
			BufferedReader br = null;
			try{
			fr = new FileReader(few);
			br = new BufferedReader(fr);
			String sCurrentLine;
			int letti=0;
			while ((sCurrentLine=br.readLine()) != null) {
				letti++;
				l.getInstance().write("lette: "+letti+" parole");
					int i=0;
					int n=0;
					l.getInstance().write("words: "+sCurrentLine);
					do
					{
						i++;
					String url2 = "https://index.docker.io/v1/search?q="+sCurrentLine+"&n=100&page="+i;
					l.getInstance().write("pagina: "+i+" di "+sCurrentLine);
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
					
					// add request header
					// con.setRequestProperty("username", "simoneerba");
					//System.out.println("content       " + con2.getHeaderField("Www-Authenticate"));
					int responseCode2 = 0;
					try {
						responseCode2 = con2.getResponseCode();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int count=0;
					if(responseCode2==200)
					{
						count=0;
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
						//System.out.println(body2);
						
						//System.out.println("\nSending 'GET' request to URL : " + url2);
						//System.out.println("\nmessaggio : " + s2);
						//System.out.println("Response Code : " + responseCode2);
						JSONObject json = new JSONObject(body2);
						JSONArray array = json.getJSONArray("results");
						n = json.getInt("num_pages");
						//System.out.println(array.length());
						
						for(int j=0;j<array.length();j++)
						{
							String name=array.getJSONObject(j).getString("name");
							String user="";
							int a=name.indexOf("/");
							if(a==-1)
							{
								user="library";
							}
							else
							{
								user=name.substring(0, a);
							}
							if(!u.getC().contains(user))
							{
								u.addUser(user);
								downloadUser(user);
							}
							/*int a=name.indexOf("/");
							if(a!=-1)
							{
								String user=name.substring(0,a);
								if(!existInFile(user,english))
								{
									append(user,english);
								}
							}*/
							/*String desc=array.getJSONObject(j).getString("description");

							 String[] arr = desc.split(" ");    

							 for ( String ss : arr) {
								 	System.out.println("se esiste");
							       if(!existInFile(ss,english))
							       {
							    	   System.out.println("controllato");
							    	   append(ss,english);
							       }
							  }*/
						}
					}
					else
					{
						if(responseCode2!=200)
						{
							count++;
							if(count>6)
							{
								i=n;
							}
						}
					}
					}
					while(i<n);
					//append(String.valueOf(line),lines);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}

	private void downloadUser(String user) {
		// TODO Auto-generated method stub
		l.getInstance().write("Found new user from all english words: "+user);
		//commit ad ogni nuovo utente
		Transaction t=GraphOperations.getInstance().getGraph().beginTx();
		int n = 0;
		int numpages = 0;
		do {
			String url2 = "https://index.docker.io/v1/search?q=" + user + "&page=" + n + "&n=100";
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

			// add request header
			// con.setRequestProperty("username", "simoneerba");
			// System.out.println("content " +
			// con2.getHeaderField("Www-Authenticate"));
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
			// System.out.println(body2);

			// System.out.println("\nSending 'GET' request to URL : " +
			// url2);
			// System.out.println("\nmessaggio : " + s2);
			// System.out.println("Response Code : " + responseCode2);
			JSONObject json = new JSONObject(body2);
			numpages = json.getInt("num_pages");
			JSONArray array = json.getJSONArray("results");
		//	System.out.println(array.length());

			for (int j = 0; j < array.length(); j++) {
				String name = array.getJSONObject(j).getString("name");
				int ind=name.indexOf("/");
				String user2="";
				if(ind==-1)
				{
					user2="library";
				}
				else
				{
					user2=name.substring(0, ind);
				}

				if(user2.equals(user))
						pullImage(name);	
				}
			n++;
		} while (n < numpages);
		t.success();
		t.close();
	
	}
	private void pullImage(String name) {

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
		// add request header
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
			// add request header
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
					System.out.println(i + "    "+name+"      " + tag);
					pullTag(name, tag, token);
				}
			}
		}

	}

	private void pullTag(String nome, String tag, String token) {
		// TODO Auto-generated method stub
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
		
		GraphOperations.getInstance().insertSingle(name, surname, tag, s, layers2,history);
	}
}
