public class OffByN implements CharacterComparator{
    int N;
    public OffByN(int N) {
       this.N = N;
    }
    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        return diff == N || diff == -N;
    }
}
