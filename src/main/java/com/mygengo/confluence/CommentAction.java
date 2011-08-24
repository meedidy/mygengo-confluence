package com.mygengo.confluence;

import java.util.*;
import com.atlassian.confluence.json.parser.*;

/**
 * Form for commenting on jobs.
 */
public class CommentAction extends MyGengoActionSupport
{
	public String execute()
	{ 	
		try
		{
			super.execute();

			Iterator<Comment> i = mJob.getThread().iterator();
			while(i.hasNext())
				i.next().setNew(false);
			mDatabase.merge(mJob);

			return "success";
		}
		catch(RuntimeException e)
		{
			addActionError(e.getMessage());
			return "error";
		}
	}

	public String submit()
	{
		super.execute();

		try
		{
			Comment c = new Comment();

			c.setBody(mHttpContext.getRequest().getParameter("comment"));

			c = mTranslationService.comment(mCredentials,mJob,c);
			mJob.getThread().add(0,c);

			mDatabase.merge(mJob);

			addActionMessage("Comment posted");
			return "success";
		}
		catch(RuntimeException e)
		{
			addActionError("Failed to post comment: " + e.getMessage());
			return "error";
		}
	}
}
