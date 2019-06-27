package me.ilnicki.bg.lwjgl3opengl;

import java.util.HashMap;

//TODO: Move to config.
class SegmentSchematics {
    private final HashMap<Character, boolean[]> schematics = new HashMap<>();
    private final boolean[] nullChar;

    SegmentSchematics() {
        schematics.put(null, nullChar = new boolean[]{false, false, false, false, false, false, false});
        schematics.put('0', new boolean[]{true, true, true, true, true, true, false});
        schematics.put('1', new boolean[]{false, true, true, false, false, false, false});
        schematics.put('2', new boolean[]{true, true, false, true, true, false, true});
        schematics.put('3', new boolean[]{true, true, true, true, false, false, true});
        schematics.put('4', new boolean[]{false, true, true, false, false, true, true});
        schematics.put('5', new boolean[]{true, false, true, true, false, true, true});
        schematics.put('6', new boolean[]{true, false, true, true, true, true, true});
        schematics.put('7', new boolean[]{true, true, true, false, false, false, false});
        schematics.put('8', new boolean[]{true, true, true, true, true, true, true});
        schematics.put('9', new boolean[]{true, true, true, true, false, true, true});
        /*                                     A      B      C      D      E      F      G      H      I    */
        schematics.put('A', new boolean[]{true, true, true, false, true, true, true, false, false});
        schematics.put('B', new boolean[]{false, false, false, true, true, true, true, true, true});
        schematics.put('C', new boolean[]{true, false, false, true, true, true, false, false, false});
        schematics.put('D', new boolean[]{false, false, true, true, true, true, false, true, false});
        schematics.put('E', new boolean[]{true, false, false, true, true, true, true, false, false});
        schematics.put('F', new boolean[]{true, false, false, false, true, true, true, false, false});
        schematics.put('G', new boolean[]{true, false, true, true, true, true, false, false, false});
        schematics.put('H', new boolean[]{false, true, true, false, true, true, true, false, false});
        schematics.put('I', new boolean[]{false, true, true, false, false, false, false, false, false});
        schematics.put('J', new boolean[]{false, true, true, true, true, false, false, false, false});
        schematics.put('K', new boolean[]{false, true, false, false, true, true, true, false, true});
        schematics.put('L', new boolean[]{false, false, false, true, true, true, false, false, false});
        schematics.put('M', new boolean[]{false, false, false, false, false, false, false, false, false});
        schematics.put('N', new boolean[]{false, false, true, false, true, true, false, true, false});
        schematics.put('O', new boolean[]{true, true, true, true, true, true, false, false, false});
        schematics.put('P', new boolean[]{true, true, false, false, true, true, true, false, false});
        schematics.put('Q', new boolean[]{true, true, true, true, true, true, true, false, false});
        schematics.put('R', new boolean[]{true, true, false, false, true, true, true, false, true});
        schematics.put('S', new boolean[]{true, false, true, true, false, true, true, false, false});
        schematics.put('T', new boolean[]{false, false, false, true, true, true, true, false, false});
        schematics.put('U', new boolean[]{false, true, true, true, true, true, false, false, false});
        schematics.put('V', new boolean[]{false, true, true, false, false, true, false, false, true});
        schematics.put('W', new boolean[]{false, false, false, false, false, false, false, false, false});
        schematics.put('X', new boolean[]{false, false, false, false, false, false, false, false, false});
        schematics.put('Y', new boolean[]{false, true, true, false, false, false, false, true, false});
        schematics.put('Z', new boolean[]{false, false, false, false, false, false, false, false, false});
        schematics.put(' ', new boolean[]{false, false, false, false, false, false, false, false, false});
        schematics.put('-', new boolean[]{false, false, false, false, false, false, true, false, false});
    }

    boolean[] get(Character chr) {
        return schematics.getOrDefault(chr, nullChar);
    }

    boolean[] get(Integer digit) {
        if (digit == null) {
            return schematics.get(null);
        } else {
            return schematics.get(digit.toString().charAt(0));
        }
    }
}