package edu.ohiou.lev_neiman.jung.volume_render.event;

import java.util.LinkedList;

/**
 * <p>Title: Scientific Volume Rendering</p>
 *
 * <p>Description: Lev Neiman's Summer Job</p>
 *
 * <p>Copyright: Copyright (c) 2008, Lev A. Neiman</p>
 *
 * <p>Company: Dr. Peter Jung</p>
 *
 * @author Lev A. Neiman
 * @version 1.0
 */
public class RenderEventQueue
{
    public enum Event
    {
        LOAD_VOLUME, SHOW_VOLUME, HIDE_VOLUME, USE_GAUSSIAN, USE_LINEAR, UPDATE_TEXTURE_SLICES, TAKE_SCREENSHOT, SCREENSHOT_TAKEN,
                MAP_TAKE_SCREENSHOT
    }


    private LinkedList<Event> queue = new LinkedList<Event> ();

    public RenderEventQueue()
    {

    }

    public synchronized boolean hasEvents()
    {
        return queue.size() > 0 ? true : false;
    }

    public synchronized void addEvent( Event event )
    {
        queue.add( event );
    }

    public synchronized Event nextEvent()
    {
        return queue.removeFirst();
    }
}
