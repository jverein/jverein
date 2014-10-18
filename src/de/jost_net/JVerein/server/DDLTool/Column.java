package de.jost_net.JVerein.server.DDLTool;

import de.jost_net.JVerein.server.DDLTool.AbstractDDLUpdate.COLTYPE;

public class Column
{
  private String name;

  private COLTYPE type;

  private int len;

  private String defaultvalue;

  private boolean notnull;

  private boolean autoincrement;

  public Column(String name, COLTYPE type, int len, String defaultvalue,
      boolean notnull, boolean autoincrement)
  {
    this.name = name;
    this.type = type;
    this.len = len;
    this.defaultvalue = defaultvalue;
    this.notnull = notnull;
    this.autoincrement = autoincrement;
  }

  public boolean isAutoincrement()
  {
    return autoincrement;
  }

  public String getName()
  {
    return name;
  }

  public COLTYPE getType()
  {
    return type;
  }

  public int getLen()
  {
    return len;
  }

  public String getDefaultvalue()
  {
    return defaultvalue;
  }

  public boolean isNotnull()
  {
    return notnull;
  }

}
