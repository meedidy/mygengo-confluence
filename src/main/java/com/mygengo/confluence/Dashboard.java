package com.mygengo.confluence;

import java.util.*;
import com.atlassian.renderer.v2.macro.*;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;

class Dashboard extends BaseMacro
{
	private com.mygengo.confluence.Database mDatabase;

	public Dashboard(com.mygengo.confluence.Database s)
	{
		mDatabase = s;
	}

	public boolean isInline() { return false; }
	public boolean hasBody() { return false; }
	public RenderMode getBodyRenderMode() { return RenderMode.NO_RENDER; }

	public String execute(Map params, String body, RenderContext renderContext) throws MacroException
	{
		Map context = MacroUtils.defaultVelocityContext();
    context.put("jobs",mDatabase.all());

		return VelocityUtils.getRenderedTemplate("templates/my-dashboard.vm",context);
	}
}
