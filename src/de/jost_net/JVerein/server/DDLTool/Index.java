package de.jost_net.JVerein.server.DDLTool;

import java.util.ArrayList;

public class Index
{
  private String name;

  private boolean unique;

  private ArrayList<Column> columns;

  public Index(String name, boolean unique)
  {
    this.name = name;
    this.unique = unique;
    this.columns = new ArrayList<>();
  }

  public void add(Column column)
  {
    columns.add(column);
  }

  public String getName()
  {
    return name;
  }

  public boolean isUnique()
  {
    return unique;
  }

  public String getCreateString()
  {
    StringBuffer sb = new StringBuffer();
    if (isUnique())
    {
      sb.append("UNIQUE (");
      int i = 0;
      for (Column c : columns)
      {
        if (i > 0)
        {
          sb.append(",");
          sb.append(c.getName());
        }
      }
      sb.append("), ");
    }
    return sb.toString();
  }

  public String getCreateIndex(String table)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("CREATE ");
    if (isUnique())
    {
      sb.append("UNIQUE ");
    }
    sb.append("INDEX ");
    sb.append(name);
    sb.append(" on ");
    sb.append(table);
    sb.append("(");
    int i = 0;
    for (Column c : columns)
    {
      if (i > 0)
      {
        sb.append(",");
      }
      sb.append(c.getName());
      i++;
    }
    sb.append(");\n");
    return sb.toString();
  }
}
