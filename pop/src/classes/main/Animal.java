package classes.main;


import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Animal implements IMapElement{
    private Vector2d position;
    private MapDirection direction; // {0,1,...,7}
    private long energy; // days till dead, energy = 0 -> died
    private final long startingEnergy;
    private final DNA dna;
    private final Map map;
    private final int moveEnergy;
    private int lifeLength = 0;
    private int kidCount = 0;
    private LinkedList<Animal> kids = new LinkedList<>();
    private Animal kid;
    private int death = 0;
    private int dCount = 0;

    private final LinkedList<IPositionChangedObserver> observersList = new LinkedList<>();

    public Animal(Map map,Vector2d initialAnimalPosition, long energy,long initialEnergy, DNA dna,int moveEnergy){
        this.direction = MapDirection.rand();
        this.position = initialAnimalPosition;
        this.map = map;
        this.startingEnergy = initialEnergy;
        this.energy = energy;
        this.dna = dna;
        this.moveEnergy = moveEnergy;

    }
    public long getEnergy(){
        return this.energy;
    }
    public void setEnergy(long energy){this.energy = energy;}
    public void increaseEnergy(long energy){this.energy += energy;}
    public int getKids(){
        return this.kidCount;
    }

    public int getKidCount(){return this.kidCount;}
    public void setKidCount(int kidCount){this.kidCount = kidCount;}
    public void increaseKidCount(){this.kidCount++;}
    public LinkedList<Animal> getKidsList(){return this.kids;}
    public void setKidsList(LinkedList<Animal> list){this.kids = list;}
    public void addKid(Animal kid){this.kids.add(kid);}

    public DNA getGenes(){
        return this.dna;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public void addObserver(IPositionChangedObserver observer){
        this.observersList.add(observer);
    }
    public void removeObserver(IPositionChangedObserver observer) {
        this.observersList.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangedObserver observer : this.observersList){
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }
    public String toString(){
        return this.direction.toString();
    }

    public void move(){
        this.energy -= this.moveEnergy;
        int rFactor = this.dna.getRandomGene();
        this.direction = this.direction.rotateBy(rFactor);
        Vector2d newPosition = this.position.add(this.direction.toUnitVector());
        newPosition = newPosition.wrapAround(this.map);
        this.positionChanged(this.getPosition(), newPosition);
        this.position = newPosition;
    }

    public int getLifeLength(){
        return this.lifeLength;
    }
    public void increaseLifeLength(){
        this.lifeLength++;
    }

    public int getDescentCount(){
        List<Animal> visited = new ArrayList<>();
        LinkedList<Animal> que = new LinkedList<>();
        que.add(this);
        while(que.size()>0){
            Animal animal = que.pop();
            if(visited.contains(animal)){
                continue;
            }
            visited.add(animal);
            if(animal.kids != null){
                que.addAll(animal.kids);
            }
        }
        return visited.size() - 1;
    }
    public void dead(){
        this.death = this.map.getDay();
    }
    public int getDeathDay(){
        if(this.death > 0){
            return this.death;
        }
        return -1;
    }


}
