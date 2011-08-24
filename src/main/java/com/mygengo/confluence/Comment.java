package com.mygengo.confluence;

import java.io.Serializable;
import com.atlassian.confluence.json.parser.*;
import java.util.*;
import java.text.*;

/**
 * Model of a comment in a thread linked to a job.
 */
public class Comment implements Serializable
{
	private Long mCtime;
	private String mAuthor;
	private String mBody;
	private Boolean mNew;

	public Comment()
	{
		return;
	}

	public Comment(JSONObject o)
	{
		try
		{
			if(o.has("body"))		setBody(o.getString("body"));
			if(o.has("author"))	setAuthor(o.getString("author"));

			if(o.has("ctime"))
			{
				try
				{
					setCtime((new Long(o.getString("ctime")) * 1000));
				}
				catch(NumberFormatException e)
				{
					DateFormat df = DateFormat.getInstance();

					try
					{
						Date d = df.parse(o.getString("ctime"));
						setCtime(d.getTime());
					}
					catch(ParseException ee)
					{
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MILLISECOND, -cal.get(Calendar.DST_OFFSET) - cal.get(Calendar.ZONE_OFFSET));
						setCtime(cal.getTimeInMillis());
					}
				}
			}

			setNew(true);
		}
		catch(JSONException e)
		{
			;
		}
	}

	public String getCtime() 	{  return DateFormat.getDateInstance(DateFormat.LONG).format(new Date(mCtime)); }
	public String getAuthor()	{  return mAuthor; }
	public String getBody() 	{  return mBody; }
	public Boolean getNew() 	{  return mNew; }

	public void setCtime(Long x) 		{  mCtime = x; }
	public void setAuthor(String x)	{  mAuthor = x; }
	public void setBody(String x) 	{  mBody = x; }
	public void setNew(Boolean x) 	{  mNew = x; }
}
