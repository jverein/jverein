package de.jost_net.JVerein.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryAnalyzer extends Thread
{

  @Override
  public void run()
  {
    while (1 == 1)
    {
      // System.out.println(Runtime.getRuntime().freeMemory());
      MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
      MemoryUsage heap = memBean.getHeapMemoryUsage();
      MemoryUsage nonheap = memBean.getNonHeapMemoryUsage();
      System.out.println(heap + " " + nonheap);
      try
      {
        Thread.sleep(2000);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

}
