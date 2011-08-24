package com.mygengo.confluence;

import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;
import java.text.DateFormat;

import com.atlassian.confluence.json.parser.*;

/**
 * Model of a running translation job.
 * Mirrors most of myGengo's job payload - 
 * adds methods to fetch jobs from remote servers
 * and update jobs in the database (Bandana)
 */
public class Job implements Serializable
{
	private Integer mJobId;
	private String mSlug;
	private String mBodySrc;
	private String mBodyTgt;
	private Language mLcSrc;
	private Language mLcTgt;
	private Integer mUnitCount;
	private String mTier;
	private String mCredits;
	private String mStatus;
	private String mCaptchaUrl;
	private Long mCtime;
	private Long mAtime;
	private List<Comment> mThread;

	public Job()
	{
		setAtime((new Date()).getTime());
		setThread(new LinkedList<Comment>());

		return;
	}

	public Job(JSONObject o)
	{
		this();
		
		try
		{
			if(o.has("job_id")) 			setJobId(o.getInt("job_id"));
			if(o.has("slug"))					setSlug(o.getString("slug"));
			if(o.has("body_src"))			setBodySrc(o.getString("body_src"));
			if(o.has("body_tgt"))			setBodyTgt(o.getString("body_tgt"));
			if(o.has("lc_src"))				setLcSrc(new Language(o.getString("lc_src")));
			if(o.has("lc_tgt"))				setLcTgt(new Language(o.getString("lc_tgt")));
			if(o.has("unit_count"))		setUnitCount(o.getInt("unit_count"));
			if(o.has("tier"))					setTier(o.getString("tier"));
			if(o.has("credits"))			setCredits(o.getString("credits"));
			if(o.has("status"))				setStatus(o.getString("status"));
			if(o.has("captcha_url"))	setCaptchaUrl(o.getString("captcha_url"));
			if(o.has("ctime"))				setCtime((new Long(o.getString("ctime")) * 1000));
		}
		catch(JSONException e)
		{
			;
		}
	}

	/**
	 * Convert the JSON array recvied from remote to a thread.
	 */
	public void setThread(JSONArray a)
	{
		try
		{
			int i = a.length();
			while(i-- != 0)
				getThread().add(new Comment(a.getJSONObject(i)));
		}
		catch(JSONException e)
		{
			;
		}
	}

	/**
	 * Convert to JSON for submit as new translation job.
	 */
	public String toJSON() throws RuntimeException
	{
		try
		{
			JSONObject ret = new JSONObject();

			ret.put("body_src",getBodySrc());
			ret.put("lc_src",getLcSrc().getLc());
			ret.put("lc_tgt",getLcTgt().getLc());
			ret.put("tier",getTier());

			return ret.toString();
		}
		catch(JSONException e)
		{
			throw new RuntimeException(e);
		}
	}

	// boring setter/getter
	public Integer getJobId() 				{ return mJobId; }
	public String getSlug() 					{ return mSlug; }
	public String getBodySrc() 				{ return mBodySrc; }
	public String getBodyTgt() 				{ return mBodyTgt; }
	public Language getLcSrc() 				{ return mLcSrc; }
	public Language getLcTgt() 				{ return mLcTgt; }
	public Integer getUnitCount() 		{ return mUnitCount; }
	public String getTier() 					{ return mTier; }
	public String getCredits() 				{ return mCredits; }
	public String getStatus() 				{ return mStatus; }
	public String getCaptchaUrl() 		{ return mCaptchaUrl; }
	public String getCtime() 					{ return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(mCtime)); }
	public String getAtime() 					{ return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(mAtime)); }
	public List<Comment> getThread()	{ return mThread; }

	public void setJobId(Integer a) 				{ mJobId = a; }
	public void setSlug(String a) 					{ mSlug = a; }
	public void setBodySrc(String a) 				{ mBodySrc = a; }
	public void setBodyTgt(String a) 				{ mBodyTgt = a; }
	public void setLcSrc(Language a) 				{ mLcSrc = a; }
	public void setLcTgt(Language a) 				{ mLcTgt = a; }
	public void setUnitCount(Integer a) 		{ mUnitCount = a; }
	public void setTier(String a) 					{ mTier = a; }
	public void setCredits(String a)	 			{ mCredits = a; }
	public void setStatus(String a) 				{ mStatus = a; }
	public void setCaptchaUrl(String a) 		{ mCaptchaUrl = a; }
	public void setCtime(Long a) 				{ mCtime = a; }
	public void setAtime(Long a) 				{ mAtime = a; }
	public void setThread(List<Comment> a) 	{ mThread = a; }
}
