package com.mygengo.confluence;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.atlassian.confluence.json.parser.*;
import com.atlassian.spring.container.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;

class CallbackServlet extends HttpServlet
{
	protected Database mDatabase;
	protected Credentials mCredentials;

	protected void doGet(HttpServletRequest req,HttpServletResponse resp)
	{
		try
		{
			resp.getOutputStream().println("<html><body>nothing to see here</body></html>");
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

		try
		{
			mCredentials = mDatabase.credentials();
			
			String job = null;
			String comment = null;

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
						if(fi.getFieldName().compareTo("job") == 0)
							job = fi.getString();
						if(fi.getFieldName().compareTo("comment") == 0)
							comment = fi.getString();
					}
				}
			}
			// for urlencoded form-data
			else
			{	
				if(req.getParameter("job") != null)
					job = req.getParameter("job");
				if(req.getParameter("comment") != null)
					comment = req.getParameter("comment");
			}

			// update job status, translated body and atime			
			if(job != null)
			{
				Job newj = new Job(new JSONObject(job));
				Job oldj = mDatabase.job(newj.getJobId());
				Calendar cal = Calendar.getInstance();

				cal.add(Calendar.MILLISECOND, -cal.get(Calendar.DST_OFFSET) - cal.get(Calendar.ZONE_OFFSET));
				oldj.setBodyTgt(newj.getBodyTgt());
				oldj.setStatus(newj.getStatus());
				oldj.setAtime(cal.getTimeInMillis());
				oldj.setCaptchaUrl(newj.getCaptchaUrl());

				mDatabase.merge(oldj);
			}

			// add a new comment
			if(comment != null)
			{
				JSONObject json = new JSONObject(comment);
				Comment c = new Comment(json);
				Job j = mDatabase.job(json.getInt("job_id"));

				if(j != null)
				{
					c.setAuthor("worker");
					c.setNew(true);

					j.getThread().add(0,c);
					mDatabase.merge(j);
				}
			}

			resp.getOutputStream().println("<html><body>nothing to see here</body></html>");
		}
		catch(RuntimeException e)
		{
			;
		}
		catch(JSONException e)
		{
			;
		}
		catch(IOException e)
		{
			;
		}
		catch(FileUploadException e)
		{
			;
		}
		return;
	}

}
