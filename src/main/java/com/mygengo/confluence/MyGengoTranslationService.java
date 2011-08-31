package com.mygengo.confluence;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.entity.*;
import org.apache.http.message.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;

import com.atlassian.confluence.json.parser.*;

class MyGengoTranslationService implements TranslationService
{
	private HttpClient mHttpClient;
	final String HOST = "api.mygengo.com";
	final String PATH = "/v1/";
	
	private class NameValuePairCollator implements Comparator
	{
		public int compare(Object a,Object b)
		{
			return ((NameValuePair)a).getName().compareTo(((NameValuePair)b).getName());
		}
	}

	public MyGengoTranslationService()
	{
		return;
	}

	public void setHttpClient(HttpClient h)
	{
		mHttpClient = h;
	}

	private String sign(List<NameValuePair> args,Credentials cred,boolean json)
	{
	  Formatter f = new Formatter();

		Collections.sort(args,new NameValuePairCollator());
		Iterator<NameValuePair> i = args.iterator();

		try
		{
			Mac hmac = Mac.getInstance("HmacSHA1");
	    hmac.init(new SecretKeySpec(cred.getPrivKey().getBytes("iso-8859-1"),"HmacSHA1"));

			if(json)
			{
				// mygengo wants the JSON object to be sorted
				String json_str = new String("{");

				while(i.hasNext())
				{
					NameValuePair nvp = i.next();

					json_str += JSONObject.quote(nvp.getName());
					json_str +=  ":";
					json_str += JSONObject.quote(nvp.getValue());

					if(i.hasNext())
						json_str += ",";
				}
				
				json_str += "}";
				for (byte b : hmac.doFinal(json_str.getBytes("iso-8859-1")))
					f.format("%02x", b);
			}
			else
				for (byte b : hmac.doFinal(URLEncodedUtils.format(args,"iso-8859-1").getBytes("iso-8859-1")))
					f.format("%02x", b);
		}
		finally
		{
			return f.toString();
		}
	}

	private String get(String url,List<NameValuePair> args,Credentials cred) throws URISyntaxException, IOException
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MILLISECOND, -cal.get(Calendar.DST_OFFSET) - cal.get(Calendar.ZONE_OFFSET));

		args.add(new BasicNameValuePair("api_key",cred.getApiKey()));
		args.add(new BasicNameValuePair("ts",String.valueOf(cal.getTimeInMillis() / 1000)));
		args.add(new BasicNameValuePair("format","json"));
		args.add(new BasicNameValuePair("api_sig",sign(args,cred,false)));

		HttpClient c = new DefaultHttpClient();
		URI tgt = new URI("http",HOST,PATH + url,URLDecoder.decode(URLEncodedUtils.format(args,"UTF-8"),"UTF-8"),null);
	
		HttpGet req = new HttpGet(tgt);
		req.addHeader("Accept","application/json");
		StringWriter out = new StringWriter();
		InputStream in = c.execute(req).getEntity().getContent();
		int b;

		while((b = in.read()) != -1)
			out.write((char)b);

		return out.toString();
	}

	private String del(String url,List<NameValuePair> args,Credentials cred) throws URISyntaxException, IOException
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MILLISECOND, -cal.get(Calendar.DST_OFFSET) - cal.get(Calendar.ZONE_OFFSET));

		args.add(new BasicNameValuePair("api_key",cred.getApiKey()));
		args.add(new BasicNameValuePair("ts",String.valueOf(cal.getTimeInMillis() / 1000)));
		args.add(new BasicNameValuePair("format","json"));
		args.add(new BasicNameValuePair("api_sig",sign(args,cred,false)));

		HttpClient c = new DefaultHttpClient();
		URI tgt = new URI("http",HOST,PATH + url,URLDecoder.decode(URLEncodedUtils.format(args,"UTF-8"),"UTF-8"),null);
	
		HttpDelete req = new HttpDelete(tgt);
		req.addHeader("Accept","application/json");
		StringWriter out = new StringWriter();
		InputStream in = c.execute(req).getEntity().getContent();
		int b;

		while((b = in.read()) != -1)
			out.write((char)b);

		return out.toString();
	}

	private String post(String url,List<NameValuePair> args,Credentials cred) throws URISyntaxException, IOException
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MILLISECOND, -cal.get(Calendar.DST_OFFSET) - cal.get(Calendar.ZONE_OFFSET));

		args.add(new BasicNameValuePair("api_key",cred.getApiKey()));
		args.add(new BasicNameValuePair("ts",String.valueOf(cal.getTimeInMillis() / 1000)));
		args.add(new BasicNameValuePair("api_sig",sign(args,cred,true)));

		HttpClient c = new DefaultHttpClient();
		URI tgt = new URI("http",HOST,PATH + url,null);
	
		HttpPost req = new HttpPost(tgt);
		req.addHeader("Accept","application/json");

		req.setEntity(new StringEntity(URLEncodedUtils.format(args,"UTF-8"),"application/x-www-form-urlencoded","UTF-8"));

		StringWriter out = new StringWriter();
		InputStream in = c.execute(req).getEntity().getContent();
		int b;

		while((b = in.read()) != -1)
			out.write((char)b);

		return out.toString();
	}

	private String put(String url,List<NameValuePair> args,Credentials cred) throws URISyntaxException, IOException
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MILLISECOND, -cal.get(Calendar.DST_OFFSET) - cal.get(Calendar.ZONE_OFFSET));

		args.add(new BasicNameValuePair("api_key",cred.getApiKey()));
		args.add(new BasicNameValuePair("ts",String.valueOf(cal.getTimeInMillis() / 1000)));
		args.add(new BasicNameValuePair("api_sig",sign(args,cred,true)));

		HttpClient c = new DefaultHttpClient();
		URI tgt = new URI("http",HOST,PATH + url,null);
	
		HttpPut req = new HttpPut(tgt);
		req.addHeader("Accept","application/json");

		req.setEntity(new StringEntity(URLEncodedUtils.format(args,"UTF-8"),"application/x-www-form-urlencoded","UTF-8"));

		StringWriter out = new StringWriter();
		InputStream in = c.execute(req).getEntity().getContent();
		int b;

		while((b = in.read()) != -1)
			out.write((char)b);

		return out.toString();
	}

	private JSONObject perform(String method,String url,List<NameValuePair> args,Credentials cred) throws RuntimeException
	{
		String payload;

		try
		{
			if(method.compareTo("GET") == 0)
				payload = get(url,args,cred);
			else if(method.compareTo("DELETE") == 0)
				payload = del(url,args,cred);
			else if(method.compareTo("POST") == 0)
				payload = post(url,args,cred);
			else if(method.compareTo("PUT") == 0)
				payload = put(url,args,cred);
			else
				throw new RuntimeException("Unsupported HTTP method: '" + method + "'");
		}
		catch(URISyntaxException e)
		{
			throw new RuntimeException("Illegal URL: '" + url + "'");
		}	
		catch(IOException e)
		{
			throw new RuntimeException("No answer from remote");
		}

		try
		{
			JSONObject o = new JSONObject(payload);
			
			if(o.getString("opstat").compareTo("ok") == 0)
				return o;
			else
				throw new RuntimeException(o.getJSONObject("err").getString("msg"));
		}
		catch(JSONException e)
		{
			throw new RuntimeException("Misformed answer from remote");
		}
	}

	public Map<String,Language> languages(Credentials cred) throws RuntimeException
	{
		Map<String,Language> ret = new HashMap<String,Language>();
		JSONArray langs;
		JSONArray pairs;
		Language l = null;

		try
		{
			langs = perform("GET","translate/service/languages",new LinkedList<NameValuePair>(),cred)
				.getJSONArray("response");

			int i = 0;
			while(langs.length() != i)
			{
				l = new Language(langs.getJSONObject(i));
				JSONObject a = new JSONObject(l);

				ret.put(l.getLc(),l);
				i++;
			}
			
			if(l != null)
			{
				pairs = perform("GET","translate/service/language_pairs",new LinkedList<NameValuePair>(),cred)
					.getJSONArray("response");
			
				i = 0;
				while(pairs.length() != i)
				{
					JSONObject t = pairs.getJSONObject(i);
					Language.Target tgt = l.new Target(t);

					if(ret.containsKey(tgt.getLcSrc()))
						ret.get(tgt.getLcSrc()).getTargets().add(tgt);
					i++;
				}
			}
		}
		catch(JSONException e)
		{
			throw new RuntimeException("Misformed answer from remote");
		}
		
		return ret;
	}

	public String balance(Credentials cred) throws RuntimeException
	{
		try
		{
			return perform("GET","account/balance",new LinkedList<NameValuePair>(),cred)
				.getJSONObject("response")
				.getString("credits");
		}
		catch(JSONException e)
		{
			throw new RuntimeException("Misformed answer from remote");
		}
	}

	public Job add(Credentials cred,Job j,Boolean auto_appr) throws RuntimeException
	{
		try
		{
			LinkedList<NameValuePair> opts = new LinkedList<NameValuePair>();
			JSONObject data = new JSONObject();
			JSONObject job = new JSONObject();

			job.put("body_src",j.getBodySrc());
			job.put("lc_src",j.getLcSrc().getLc());
			job.put("lc_tgt",j.getLcTgt().getLc());
			job.put("tier",j.getTier());
			if(j.getThread().size() > 0) job.put("comment",j.getThread().get(0).getBody());
			job.put("slug",j.getSlug());
			if(auto_appr) job.put("auto_approve","1");
			data.put("job",job);
			
			opts.add(new BasicNameValuePair("data",data.toString()));

			JSONObject resp = new JSONObject(perform("POST","translate/job",opts,cred).toString());
			Job newj = new Job(resp.getJSONObject("response").getJSONObject("job"));

			return newj;
		}
		catch(JSONException e)
		{	
			throw new RuntimeException("Ill-formed JSON");
		}
	}

	public List<Job> list(Credentials cred) throws RuntimeException
	{
		List<Job> ret = new LinkedList<Job>();

		try
		{
			JSONArray arr = perform("GET","translate/jobs",new LinkedList<NameValuePair>(),cred).getJSONArray("response");
			for(int i = 0; i < arr.length(); i++)
			{
				Job j = new Job(arr.getJSONObject(i));
				ret.add(job(cred,j));
			}
	
			return ret;
		}
		catch(JSONException e)
		{
			throw new RuntimeException("Misformed answer from remote (no job)");
		}
	}


	public Job job(Credentials cred,Job j) throws RuntimeException
	{
		try
		{
			return new Job(perform("GET","translate/job/" + j.getJobId().toString(),new LinkedList<NameValuePair>(),cred).getJSONObject("response").getJSONObject("job"));
		}
		catch(JSONException e)
		{
			throw new RuntimeException("Misformed answer from remote");
		}
	}

	public List<Comment> thread(Credentials cred,Job j) throws RuntimeException
	{
		try
		{
			JSONObject resp = perform("GET","translate/job/" + j.getJobId().toString() + "/comments",new LinkedList<NameValuePair>(),cred);
			List<Comment> ret = new LinkedList<Comment>();

			if(resp.getJSONObject("response").has("thread"))
			{
				JSONArray thread = resp.getJSONObject("response").getJSONArray("thread");
				int i = thread.length();

				while(i-- != 0)
					ret.add(new Comment(thread.getJSONObject(i)));
			}
			return ret;
		}
		catch(JSONException e)
		{
			throw new RuntimeException("Misformed answer from remote (no thread)");
		}
	}

	public void cancel(Credentials cred,Job j) throws RuntimeException
	{
		perform("DELETE","translate/job/" + j.getJobId().toString(),new LinkedList<NameValuePair>(),cred);
	}

	public Feedback feedback(Credentials cred,Job j) throws RuntimeException
	{
		try
		{
			return new Feedback(perform("GET","translate/job/" + j.getJobId().toString() + "/feedback",new LinkedList<NameValuePair>(),cred).getJSONObject("response").getJSONObject("feedback"));
		}
		catch(JSONException e)
		{
			throw new RuntimeException("Misformed answer from remote (no feedback)");
		}
	}

	public void approve(Credentials cred,Job j,String rating,String for_trans,String for_mygengo,Boolean is_public) throws RuntimeException
	{
		try
		{
			List<NameValuePair> args = new LinkedList<NameValuePair>();
			JSONObject o = new JSONObject();

			o.put("action","approve");
			o.put("rating",new Integer(rating));
			o.put("for_translator",for_trans);
			o.put("for_mygengo",for_mygengo);
			o.put("public",is_public ? "1" : "0");

			args.add(new BasicNameValuePair("data",o.toString()));

			perform("PUT","translate/job/" + j.getJobId().toString(),args,cred);
		}
		catch(JSONException e)
		{
			throw new RuntimeException("I'm too stupid to construct JSON :(");
		}

		return;
	}

	public String preview(Credentials cred,Job j)
	{
		Calendar cal = Calendar.getInstance();
		List<NameValuePair> args = new LinkedList<NameValuePair>();

		cal.add(Calendar.MILLISECOND, -cal.get(Calendar.DST_OFFSET) - cal.get(Calendar.ZONE_OFFSET));

		args.add(new BasicNameValuePair("api_key",cred.getApiKey()));
		args.add(new BasicNameValuePair("ts",String.valueOf(cal.getTimeInMillis() / 1000)));
		args.add(new BasicNameValuePair("api_sig",sign(args,cred,false)));

		return "http://" + HOST + PATH + "translate/job/" + j.getJobId() + "/preview?" + URLEncodedUtils.format(args,"UTF-8");
	}

	public Comment comment(Credentials cred,Job j,Comment c) throws RuntimeException
	{
		try
		{
			LinkedList<NameValuePair> opts = new LinkedList<NameValuePair>();
			JSONObject data = new JSONObject();
			Comment ret = new Comment();
			Calendar cal = Calendar.getInstance();

			cal.add(Calendar.MILLISECOND, -cal.get(Calendar.DST_OFFSET) - cal.get(Calendar.ZONE_OFFSET));

			data.put("body",c.getBody());
			opts.add(new BasicNameValuePair("data",data.toString()));

			perform("POST","translate/job/" + j.getJobId().toString() + "/comment",opts,cred);

			ret.setBody(c.getBody());
			ret.setAuthor("customer");
			ret.setCtime(cal.getTimeInMillis());
			return ret;
		}
		catch(JSONException e)
		{	
			throw new RuntimeException("Ill-formed JSON");
		}
	}

	public void reject(Credentials cred,Job j,String follow_up,String reason,String comment,String captcha) throws RuntimeException
	{
		try
		{
			List<NameValuePair> args = new LinkedList<NameValuePair>();
			JSONObject o = new JSONObject();

			o.put("action","reject");
			o.put("reason",reason);
			o.put("comment",comment);
			o.put("captcha",captcha);
			o.put("follow_up",follow_up);
			args.add(new BasicNameValuePair("data",o.toString()));

			perform("PUT","translate/job/" + j.getJobId().toString(),args,cred);
		}
		catch(JSONException e)
		{
			throw new RuntimeException("I'm too stupid to construct JSON :(");
		}

		return;
	}

	public void correct(Credentials cred,Job j,String c) throws RuntimeException
	{
		try
		{
			List<NameValuePair> args = new LinkedList<NameValuePair>();
			JSONObject o = new JSONObject();

			o.put("action","revise");
			o.put("comment",c);

			args.add(new BasicNameValuePair("data",o.toString()));

			perform("PUT","translate/job/" + j.getJobId().toString(),args,cred);
		}
		catch(JSONException e)
		{
			throw new RuntimeException("I'm too stupid to construct JSON :(");
		}

		return;
	}

	public void quote(Credentials cred,List<Job> js)
	{
		if(js.size() == 0) return;

		try
		{
			LinkedList<NameValuePair> opts = new LinkedList<NameValuePair>();
			JSONObject data = new JSONObject();
			JSONObject jobs = new JSONObject();
			Integer idx = 0;

			Iterator<Job> ji = js.iterator();
			while(ji.hasNext())
			{
				JSONObject job = new JSONObject();
				Job j = ji.next();

				job.put("body_src",j.getBodySrc());
				job.put("lc_src",j.getLcSrc().getLc());
				job.put("lc_tgt",j.getLcTgt().getLc());
				job.put("tier",j.getTier());
				
				jobs.put(idx.toString(),job);

				idx++;
			}

			data.put("jobs",jobs);
			opts.add(new BasicNameValuePair("data",data.toString()));
			JSONObject resp = new JSONObject(perform("POST","translate/service/quote",opts,cred).toString());

			// mygengo returns an array if one job was submit and an hash if more :(
			if(js.size() == 1)
			{
				JSONArray ary = resp.getJSONObject("response").getJSONArray("jobs");
				JSONObject job = ary.getJSONObject(0);
				Job j = js.get(0);
				
				j.setCredits(job.getString("credits"));

				return;
			}
			else
			{
				JSONObject hash = resp.getJSONObject("response").getJSONObject("jobs");

				while(idx > 0)
				{
					idx--;

					JSONObject job = hash.getJSONObject(idx.toString());
					Job j = js.get(idx);
					j.setCredits(job.getString("credits"));
				}

				return;
			}
		}
		catch(JSONException e)
		{	
			throw new RuntimeException("Ill-formed JSON");
		}
	}
}	
