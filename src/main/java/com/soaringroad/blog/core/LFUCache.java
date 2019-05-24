package com.soaringroad.blog.core;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class LFUCache {
  private static int dateCounter = 0;
  private static final class TimedValue {
      private int date;
      private int value;
      private int times;
      
      public int getDate() {
        return this.date;
      }
      public void setDate(final int date) {
        this.date = date;
      }
      public int getValue() {
        return this.value;
      }
      public void setValue(final int value) {
        this.value = value;
      }
      public int getTimes() {
        return this.times;
      }
      
      public void addTimes() {
          this.times = this.times + 1;
      }
      
  }
  private final Map<Integer,TimedValue> container;
  private final int capacity;
  /*
  * @param capacity: An integer
  */public LFUCache(final int capacity) {
    this.container = new HashMap<Integer,TimedValue>(Math.round(capacity/3.0f*4.0f));
      this.capacity = capacity;
  }

  /*
   * @param key: An integer
   * @param value: An integer
   * @return: nothing
   */
  public final void set(final int key, final int value) {
      if (!this.container.containsKey(key)) {
        remove();
      }
      TimedValue tv = this.container.get(key);
      if (tv == null) {
        tv = new TimedValue();
        this.container.put(key,tv);
      }
      tv.setValue(value);
      addCount(tv);

  }
  
  private static final void addCount(TimedValue tv) {
    tv.addTimes();
    tv.setDate(dateCounter+=1);
  }
  
  private final void remove() {
      if (this.container.size() < this.capacity) {
        return;
      }
  
      TimedValue least = null;
      Integer leastK = null;
      Integer k = 0;
      TimedValue v = null;
      Set<Entry<Integer, TimedValue>> entrySet  = this.container.entrySet();
      for (Entry<Integer, TimedValue> e : entrySet) {
        k = e.getKey();
        v = e.getValue();
        if (least == null || v.getTimes() < least.getTimes() || (v.getTimes() == least.getTimes() && v.getDate() <= least.getDate())) {
            leastK = k;
            least = v;
        }
      }
      if (leastK == null) {
          return;
      }
      this.container.remove(leastK);
  }

  /*
   * @param key: An integer
   * @return: An integer
   */
  public final int get(final int key) {
      TimedValue tv = this.container.get(key);
      if (tv == null) {
          return -1;
      }
      addCount(tv);
      return tv.getValue();
  }
}
