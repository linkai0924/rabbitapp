package net.duohuo.dhroid.eventbus;

/**
 * 事件类
 *
 * @author duohuo-jinghao
 */
public class Event {

    /**
     * 事件名
     */
    public String name;
    /**
     *
     */
    public Object[] params;
    /**
     * 事件发生时间
     */
    public long eventTime;

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
