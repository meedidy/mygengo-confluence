package com.mygengo.confluence;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.atlassian.confluence.json.parser.*;
import com.atlassian.spring.container.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;

class QuoteServlet extends HttpServlet
{
	protected Credentials mCredentials;
	protected Database mDatabase;
	protected TranslationService mTranslationService;

	protected void doGet(HttpServletRequest req,HttpServletResponse resp)
	{
		try
		{
			resp.getOutputStream().println("<div>Error: GET</div>");
		}
		catch(IOException e)
		{
			;
		}
		return;
	}

	protected void doPost(HttpServletRequest req,HttpServletResponse resp)
	{
		mDatabase = new BandanaDatabase();
		com.atlassian.spring.container.ContainerManager.autowireComponent(mDatabase);
		mTranslationService = new MyGengoTranslationService();
		com.atlassian.spring.container.ContainerManager.autowireComponent(mTranslationService);

		try
		{
			mCredentials = mDatabase.credentials();

			JSONObject targets = null;
			String content = null;
			String lang = null;

			// for multipart/form-data
			if(ServletFileUpload.isMultipartContent(req))
			{
				ServletFileUpload fu = new ServletFileUpload(new DefaultFileItemFactory());
				Iterator i = fu.parseRequest(req).iterator();

				while(i.hasNext())
				{
					FileItem fi = (FileItem)i.next();
					if(fi.isFormField())
					{
						if(fi.getFieldName().compareTo("targets") == 0)
							targets = new JSONObject(fi.getString());
						if(fi.getFieldName().compareTo("content") == 0)
							content = fi.getString();
						if(fi.getFieldName().compareTo("lang") == 0)
							lang = fi.getString();
					}
				}
			}
			// for urlencoded form-data
			else
			{	
				if(req.getParameter("targets") != null)
					targets = new JSONObject(req.getParameter("targets"));
				if(req.getParameter("content") != null)
					content = req.getParameter("content");	
				if(req.getParameter("lang") != null)
					lang = req.getParameter("lang");
			}

			if(targets != null && content != null && lang != null)
			{
				List<Job> jobs = new LinkedList<Job>();
				Iterator i = targets.keys();

				while(i.hasNext())
				{
					String k = i.next().toString();
					Job j = new Job(targets.getJSONObject(k));

					j.setBodySrc(content);
					j.setLcSrc(new Language(lang));
					j.setLcTgt(new Language(k));
					jobs.add(j);
				}
				mTranslationService.quote(mCredentials,jobs);

				Float cost = 0.0f;

				Iterator<Job> ji = jobs.iterator();
				while(ji.hasNext())
				{
					Job j = ji.next();
					cost += Float.valueOf(j.getCredits());
				}

				Formatter fmt = new Formatter(resp.getOutputStream());
				fmt.format("<div style=\"margin: 2em;\"><p style=\"font-size: 13pt;\">This jobs final cost is <b>%.2f</b> credits</p><p style=\"font-size: 13pt;\">Click 'Submit' to send this job to myGengo.</p></div>",cost);
				fmt.flush();
			}
			else
			{
				resp.getOutputStream().println("<div>args are null</div>");
			}				
		}
		catch(RuntimeException e)
		{
			System.out.println("Runtime error");
		}
		catch(JSONException e)
		{
			System.out.println("JSON error");
		}
		catch(IOException e)
		{
			System.out.println("IO error");
		}
		catch(FileUploadException e)
		{
			System.out.println("mime error");
		}
		return;
	}

}
