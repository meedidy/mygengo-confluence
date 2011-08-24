jQuery(document).ready(function ()
{
	// setup event handlers
	
	// fill tgt-lang <select>
	jQuery("#src-lang").change(function()
	{
		var e = document.getElementById("src-lang");
		var sel = e.options[e.selectedIndex];

		if(sel && myGengo.languages[sel.value] != undefined)
		{
			var l = myGengo.languages[sel.value];
			myGengo.current = l;

			var tlang = document.getElementById("tgt-lang");
			if(tlang)
			{
				var i = 0;

				tlang.innerHTML = "";
				while(myGengo.languages.known[i] != undefined)
				{
					var lc = myGengo.languages.known[i];
					var t = myGengo.current.targets[lc];
					
					if(t != undefined)
						tlang.add(new Option(myGengo.languages[lc].name,lc),undefined);
					i = i + 1;
				}
			}

			var json = document.getElementById("tgt-json");
			if(json != undefined)
				json.value = "{}";
			myGengo.selected = {};

			var tbl = document.getElementById("tgt-tbl").tBodies[0];
			if(tbl != undefined)
				tbl.innerHTML = "";

			jQuery("#tgt-lang").trigger("change");
		}

		// calc new price
		jQuery("#body").trigger('change');
	});

	// fill tgt-tier <select>
	jQuery("#tgt-lang").change(function()
	{
		var e = document.getElementById("tgt-lang");
		var sel = e.options[e.selectedIndex];
		
		if(sel && myGengo.current != undefined && myGengo.current.targets[sel.value] != undefined)
		{
			var l = myGengo.current.targets[sel.value]

			document.getElementById("tier-standard").disabled = l.standard == undefined;
			document.getElementById("tier-pro").disabled = l.pro == undefined;
			document.getElementById("tier-ultra").disabled = l.ultra == undefined;

			/*
			var tier = document.getElementById("tier");
			if(tier)
			{
				tier.innerHTML = "";

				if(l.standard != undefined)
					tier.add(new Option("Standard","standard"),undefined);
				if(l.pro != undefined)
					tier.add(new Option("Pro","pro"),undefined);
				if(l.ultra != undefined)
					tier.add(new Option("Ultra","ultra"),undefined);
			}*/
		}

		// calc new price
		jQuery("#body").trigger('change');
	});

	// "Add language" button
	jQuery("#add-lang").click(function()
	{
		var tgt = document.getElementById("tgt-lang");
		var tgt_sel = tgt.options[tgt.selectedIndex];

		if(tgt_sel.disabled)
			return;
		
		var tiers = document.getElementsByName("tier");
		var tier_sel = null;
		for(i = 0; i < tiers.length; i++)
			if(tiers[i].checked)
				tier_sel = tiers[i];

		var tbl = document.getElementById("tgt-tbl").tBodies[0];

		tbl.insertRow(tbl.rows.length);

		var row = tbl.rows[tbl.rows.length-1];
	
		// language
		row.insertCell(0);
		row.cells[0].appendChild(document.createTextNode(myGengo.languages[tgt_sel.value].name));

		// tier
		row.insertCell(1);
		row.cells[1].appendChild(document.createTextNode(tier_sel.value));

		// cost
		row.insertCell(2);
		row.cells[2].appendChild(document.createTextNode(myGengo.current.targets[tgt_sel.value][tier_sel.value].unit_price));

		// command
		var a = document.createElement("a");
		a.appendChild(document.createTextNode("delete"));
		a.onclick = function()
		{
			tbl.removeChild(row);
			delete myGengo.selected[tgt_sel.value];
			tgt_sel.disabled = false;
	
			var js = document.getElementById("tgt-json");
			if(js != undefined)
				js.value = JSON.stringify(myGengo.selected);

			// calc new price
			jQuery("#body").trigger('change');
		};
		a.href = "#";

		row.insertCell(3);
		row.cells[3].appendChild(a);

		tgt_sel.disabled = true;

		myGengo.selected[tgt_sel.value] = 
		{
			tier: tier_sel.value,
			unit_price: myGengo.current.targets[tgt_sel.value][tier_sel.value].unit_price
		};

		var json = document.getElementById("tgt-json");
		if(json != undefined)
			json.value = JSON.stringify(myGengo.selected);
	
		// calc new price
		jQuery("#body").trigger('change');
	});

	jQuery("#body").change(function()
	{
		var i = 0;
		var sum = 0.0;
		var body = document.getElementById("body");
		var norm = body.value.replace(/\s/g,' ').split(' ');

		while(myGengo.languages.known[i] != undefined)
		{
			var tgt = myGengo.selected[myGengo.languages.known[i]];
			if(tgt != undefined)
			{
				for (var j = 0; j < norm.length; j++)
					if(norm[j].length > 0)
						if(myGengo.current.unit_type == "character")
							sum += norm[j].length * parseFloat(tgt.unit_price);
						else
							sum += parseFloat(tgt.unit_price);
							
			}
			i += 1;
		}

		var label = document.getElementById("price");
		if(label != undefined)
			label.innerHTML = sum.toFixed(2);
	});

	// fill src-lang
	var slang = document.getElementById("src-lang");
	if(slang)
	{
		var i = 0;
		while(myGengo.languages.known[i] != undefined)
		{
			var l = myGengo.languages[myGengo.languages.known[i]];
			slang.add(new Option(l.name,l.lc,l.lc == myGengo.default_lang,l.lc == myGengo.default_lang),undefined);
			i = i + 1;
		}
	}

	jQuery("#src-lang").trigger('change');
});
