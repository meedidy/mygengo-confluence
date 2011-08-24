package com.mygengo.confluence;

import com.atlassian.confluence.json.parser.*;

/**
 * Model a translation feedback of a apprioved job.
 */
public class Feedback
{
	private Integer mRating;
	private String mForTrans;
	private String mForMygengo;

	public Feedback()
	{
		return;
	}

	public Feedback(JSONObject o)
	{
		this();
		
		try
		{
			if(o.has("rating")) 				setRating(o.getInt("rating"));
			if(o.has("for_translator"))	setForTrans(o.getString("for_translator"));
			if(o.has("for_mygengo"))		setForMygengo(o.getString("for_mygengo"));
		}
		catch(JSONException e)
		{
			;
		}
	}

	public Integer getRating() 		{ return mRating; }
	public String getForTrans() 	{ return mForTrans; }
	public String getForMygengo() { return mForMygengo; }

	public void setRating(Integer a) 		{ mRating = a; }
	public void setForTrans(String a) 	{ mForTrans = a; }
	public void setForMygengo(String a)	{ mForMygengo = a; }
}
