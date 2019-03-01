package occupations;

public class special extends baseOccupation{
    public special(int n){
        setAlive(true);
        setAction(false);
        switch (n) {
            case 0:
                setOccupatin("seer");
                break;
            case 1:
                setOccupatin("witch");
                break;
            case 2:
                setOccupatin("hunter");
                break;
            case 3:
                setOccupatin("sb");
                break;
            case 4:
                setOccupatin("older");
                break;
            case 5:
                setOccupatin("angle");
                break;
                default:
                    break;
        }
    }
}
