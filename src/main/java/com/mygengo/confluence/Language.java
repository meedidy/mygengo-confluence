package com.mygengo.confluence;

import java.io.Serializable;
import java.util.*;
import com.atlassian.confluence.json.parser.*;

public class Language implements Serializable
{
	public class Target implements Serializable
	{
		private String mLcSrc;
		private String mLcTgt;
		private String mTier;
		private String mCost;

		public Target()
		{
			return;
		}

		public Target(JSONObject o)
		{
			this();

			try
			{
				if(o.has("lc_tgt")) 			setLcTgt(o.getString("lc_tgt"));
				if(o.has("lc_src")) 			setLcSrc(o.getString("lc_src"));
				if(o.has("tier")) 				setTier(o.getString("tier"));
				if(o.has("unit_price")) 	setCost(o.getString("unit_price"));
			}
			catch(JSONException e)
			{
				throw new RuntimeException(e);
			}
		}

		public JSONObject toJSON()
		{
			JSONObject ret = new JSONObject();

			try
			{
				return ret.put("lc",getLcTgt()).
									 put("tier",getTier()).
									 put("unit_price",getCost());
			}
			catch(JSONException e)
			{
				return ret;
			}
		}

		public String getLcSrc() 	{ return mLcSrc; }
		public String getLcTgt() 	{ return mLcTgt; }
		public String getTier() 	{ return mTier; }
		public String getCost() 	{ return mCost; }

		public void setLcSrc(String a) 	{ mLcSrc = a; }
		public void setLcTgt(String a) 	{ mLcTgt = a; }
		public void setTier(String a) 	{ mTier = a; }
		public void setCost(String a) 	{ mCost = a; }
	};	

	private Locale mLocale;
	private List<Target> mTargets;
	private String mUnitType;

	public Language()
	{
		return;
	}

	public Language(String lc)
	{
		this();

		setLocale(new Locale(lc));
		setTargets(new LinkedList<Target>());
		setUnitType("word");

		return;
	}

	public Language(JSONObject o)
	{
		this();

		try
		{
			if(o.has("lc")) 				setLocale(new Locale(o.getString("lc")));
			if(o.has("unit_type")) 	setUnitType(o.getString("unit_type"));
			setTargets(new LinkedList<Target>());
		}
		catch(JSONException e)
		{
			throw new RuntimeException(e);
		}
	}

	/*
	 *  src  tgt  tier  
	 * =================================
	 * [ja]-+
	 * 			|
	 * 			[en]-+
	 * 			|		 |
	 * 			|		 [pro]-+
	 * 			|		 |		 |
	 *  		|		 |		 [lc]
	 *		  |		 |		 [tier]
	 *  		|		 |		 [unit_price]
	 *  		|		 |
	 *  		|		 [ultra]-+
	 *  		|		  			 |
	 *  		|		 	  		 [lc]
	 *  		|		 		  	 [tier]
	 *  		|		 		 	   [unit_price]
	 *  		|		 
	 *			[de]-+
	 *			|		 |
	 *			|		 [machine]-+
	 *			|		 |				 |
	 *			|		 |				 [lc]
	 *			|		 | 				 [tier]
	 *			|		 |  			 [unit_price]
	 *			|		 |
	 */
	public JSONObject toJSON()
	{
		JSONObject ret = new JSONObject();

		try
		{
			JSONObject tgt = new JSONObject();
			Iterator<Target> i = getTargets().iterator();

			while(i.hasNext())
			{
				Target t = i.next();
				if(!tgt.has(t.getLcTgt()))
					tgt.put(t.getLcTgt(),new JSONObject());
				tgt.getJSONObject(t.getLcTgt()).put(t.getTier(),t.toJSON());
			}

			return ret.put("targets",tgt)
								.put("lc",getLc())
								.put("name",getName())
								.put("unit_type",getUnitType());
		}
		catch(JSONException e)
		{
			return ret;
		}
	}

	public Locale getLocale()						{ return mLocale; }
	public String getLc() 							{ return mLocale.getLanguage(); }
	public String getName() 						{ return mLocale.getDisplayName(); }
	public List<Target> getTargets()		{ return mTargets; }
	public String getUnitType()					{ return mUnitType; }

	public void setLocale(Locale l) 				{	mLocale = l; }
	public void setTargets(List<Target> l)	{ mTargets = l; }
	public void setUnitType(String s)				{ mUnitType = s; }
}
