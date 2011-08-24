package com.mygengo.confluence;

import java.util.*;
import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.opensymphony.webwork.ServletActionContext;
import com.atlassian.confluence.web.context.HttpContext;

/**
 * Display various management forms to cancel, approve or (re)view jobs.
 */
public class ManageAction extends MyGengoActionSupport
{
	public String execute()
	{
		return "success";
	}
}
