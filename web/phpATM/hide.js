function Hide(id) 
{
	var itm = null;
	if (document.getElementById)
	{
		itm = document.getElementById(id);
	}
	else if (document.all)
	{
		itm = document.all[id];
	}
	else if (document.layers)
	{
		itm = document.layers[id];
	}

	if (!itm)
	{
		// do nothing
	}
	else if (itm.style)
	{
		if (itm.style.display != "none")
		{
			itm.style.display = "none";
		}
		else
		{
			itm.style.display = "";
		}
	}
	else
	{
		itm.visibility = "hidden";
	}
}
