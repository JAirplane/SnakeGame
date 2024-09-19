package org.jeffersonairplane.model;

/**
* Provides method to change snake state.
* Basically all power ups implements this.
*/
public interface Influencing {
	/**
	* changes snake state.
	* @param snake is a snake itself.
	*/
    void influence(Snake snake);
}
