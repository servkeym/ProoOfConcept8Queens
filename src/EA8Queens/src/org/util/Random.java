package org.util;

/**
 *
 * @author servkey
 */
public class Random {
    /* internal variables required for random number generation */
    private static float  Rseed;                   /* random seed */
    private static double[] oldrand = new double[55];                /* array of 55 temporal random number*/
    private static int jrand;                         /* index for the array of random numbers */
    private static int rndcalcflag;
    private static double rndx2;

    /* Create next batch of 55 random numbers */

    private static void advance_random()
    {
        int j1;
        double new_random;
        for(j1 = 0; j1 < 24; j1++)
        {
            new_random = oldrand[j1] - oldrand[j1+31];
            if(new_random < 0.0) new_random = new_random + 1.0;
                oldrand[j1] = new_random;
        }

        for(j1 = 24; j1 < 55; j1++)
        {
            new_random = oldrand [j1] - oldrand [j1-24];
            if(new_random < 0.0) new_random = new_random + 1.0;
                oldrand[j1] = new_random;
        }
    }

    /* flip-coin: returns 1 with probability prob */
    public static int flip(float prob)
    {
       // randomperc();
        if(randomperc() <= prob)
            return(1);
        else
            return(0);
    }

    /* initialization routine for randomnormaldeviate */

    public static void initrandomnormaldeviate()
    {
        rndcalcflag = 1;
    }

    /* normal noise with specified mean & std dev: mu & sigma */

    public static double N(double mu,double sigma)
    {
      //andomnormaldeviate();
      return((randomnormaldeviate()*sigma) + mu);
    }

    /* Initialize random numbers batch */

    public static void randomize()

    {
      int j1;

      for(j1=0; j1<=54; j1++)
        oldrand[j1] = 0.0;

      jrand=0;

      warmup_random(getRseed());
    }

    /* random normal deviate after ACM algorithm 267 / Box-Muller Method */

    private static double randomnormaldeviate()

    {
       // sqrt(), log(), sin(), cos();
      //randomperc();
      double t, rndx1;

      if(rndcalcflag == 0)
      {
          rndx1 = Math.sqrt(- 2.0 * Math.log((double) randomperc()));
          t = 6.2831853072 * (double) randomperc();
          rndx2 = Math.sin(t);
          rndcalcflag = 0;
          return(rndx1 * Math.cos(t));
      }
      else

        {
          rndcalcflag = 1;
          return(rndx2);
        }

    }

    /* Fetch a single random number between 0.0 and 1.0 - Subtractive Method */
    /* See Knuth, D. (1969), v. 2 for details */
    /* name changed from random() to avoid library conflicts on some machines*/

    private static float randomperc()
    {
      jrand++;
      if(jrand >= 55)
        {
          jrand = 1;
          advance_random();
        }
      return((float) oldrand[jrand]);
    }

    /* Pick a random integer between low and high */

    public static int rnd(int low, int high)
    {
      int i;
      //randomperc();

      if(low >= high)
        i = low;
      else
      {
          i =   (int) ((randomperc() * (high - low + 1)) + low);
          if(i > high) i = high;
      }
      return(i);
    }

    /* real random number between specified limits */
    public static float rndreal(float lo ,float hi)
    {
          return((randomperc() * (hi - lo)) + lo);
    }

    /* Get random off and running */
    private static void warmup_random(float random_seed)
    {
      int j1, ii;
      double new_random, prev_random;
      //System.out.println("Random seed " + random_seed);
      oldrand[54] = random_seed;
      new_random = 0.000000001;
      prev_random = random_seed;

      for(j1 = 1 ; j1 <= 54; j1++)
      {
         ii = (21*j1)%54;
         oldrand[ii] = new_random;

         new_random = prev_random-new_random;
         //System.out.println( " prev -> " + prev_random + " new -> " + new_random);
         if(new_random < 0.0) new_random = new_random + 1.0;
            prev_random = oldrand[ii];
     }

     advance_random();
     advance_random();
     advance_random();
     jrand = 0;
    }

    /**
     * @return the Rseed
     */
    public static float getRseed() {
        return Rseed;
    }

    /**
     * @param aRseed the Rseed to set
     */
    public static void setRseed(float aRseed) {
        Rseed = aRseed;
    }
}
