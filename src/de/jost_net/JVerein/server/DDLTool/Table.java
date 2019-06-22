package de.jost_net.JVerein.server.DDLTool;

import java.util.ArrayList;

public class Table
{
  private String name;

  private ArrayList<Column> columns;

  private Column[] primarykey;

  private ArrayList<Index> indices;

  public Table(String name)
  {
    this.name = name;
    this.columns = new ArrayList<>();
    this.indices = new ArrayList<>();
  }

  public String getName()
  {
    return name;
  }

  public void add(Column column)
  {
    columns.add(column);
  }

  public void add(Index index)
  {
    indices.add(index);
  }

  public void setPrimaryKey(Column... columns)
  {
    this.primarykey = columns;
  }

  public ArrayList<Column> getColumns()
  {
    return columns;
  }

  public ArrayList<Index> getIndices()
  {
    return indices;
  }

  public Column[] getPrimaryKey()
  {
    return primarykey;
  }

}
