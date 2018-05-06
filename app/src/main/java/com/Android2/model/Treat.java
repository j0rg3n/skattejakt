package com.Android2.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Something that can be found / unlocked in the map.
 *
 * Initial list:
 * Photo: A hint of a problem solution or POI location, or just a pretty picture
 * Map fragment: Hand-drawn (but maybe autogenerated fake-handdrawn) map fragment, with reduced
 * detail level and scope. Primarily a hint to find one or more POIs.
 * Compass: Direction and distance to a POI
 * Note: A riddle of a POI location, or just a story piece. Or pretty poem.
 * Audio Tape: Same as above, but as audio.
 * Camera: Same as above, but with video, too.
 * Button: Can be connected to gates, to unlock Gates. Should be nice with sound, visuals, animation.
 * Key: Functions together with Lock treats to unlock Gates. Also should have some visuals and sound, ideally.
 * Lock: Like a button, but requires one or more Keys.
 * MapZone: When entered, unlocks Gates.
 */
public abstract class Treat {
    int id; //< GUID within the project.
    String name; //< Descriptive name. Simplifies things if always present, but might be generic, like "Video #2". TODO: Use localizable name.
    LatLng location; //< May be null
}
