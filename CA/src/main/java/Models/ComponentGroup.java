package Models;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static Utilities.Utilities.validIndex;

/**
 * A class containing details on groups of components
 */
public class ComponentGroup {
    private List<PixelGroup> groups;
    private String name;
    private Color color;
    private int[] groupPixels;
    private int sourceX,sourceY;
    private double tolerance, noise;

    public ComponentGroup(String name,Color color) {
        this.groups = new ArrayList<>();
        this.name = name;
        this.color = color;
    }

    public void setSliders(double tolerance, double noise){
        this.tolerance = tolerance;
        this.noise = noise;
    }

    public double getTolerance() {
        return tolerance;
    }

    public double getNoise() {
        return noise;
    }

    public int getSourceX() {
        return sourceX;
    }

    public int getSourceY() {
        return sourceY;
    }

    public PixelGroup getPixelGroup(int index){
        return validIndex(index,groups) ? groups.get(index):null;
    }

    /**
     * Finds a pixel group by its root
     * @param root root of group
     * @return correspondingpixel group
     */
    public PixelGroup getPixelGroupByRoot(int root){
        for(PixelGroup group:groups){
            if(group.getRoot()==root) return group;
        }
        return null;
    }
    public void setSource(int x,int y){
        sourceX = x;
        sourceY = y;
    }

    /**
     * Adds a group so that the list remains sorted from smallest to highest
     * @param pixelGroup group to be added
     * @return a boolean if the addition is successful
     */
    public boolean addPixelGroup(PixelGroup pixelGroup){
        int size = pixelGroup.getSize();
        if(groups.isEmpty()||groups.get(groups.size()-1).getSize()<=size) return groups.add(pixelGroup);
        else if(groups.get(0).getSize()>=size){groups.add(0,pixelGroup); return true;}
        else
        {
            for(int i = 0;i<groups.size()-1;++i){
                if(size>groups.get(i).getSize()&&size<groups.get(i+1).getSize()){
                    groups.add(i,pixelGroup);
                    return true;
                }
            }

        }
//needs to be fixed
        return false;
    }

    public boolean removePixelGroup(PixelGroup pg){
        groupPixels[pg.getRoot()]=-1;
        return groups.remove(pg);
    }

    public int[] getGroupPixels() {
        return groupPixels;
    }

    public void setGroupPixels(int[] groupPixels) {
        this.groupPixels = groupPixels;
    }

    public boolean removePixelGroup(int index){
        return groups.remove(index) == null;
    }

    public List<PixelGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<PixelGroup> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                "\nColour: " + color +
                "\n" + groups.size() + " Groups";
    }
}
