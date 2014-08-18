/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz;

import java.util.Random;

/**
 *
 * @author saljack
 */
public class AnswerPermutationImpl implements AnswerPermutation {

    private static final int[][] permutation = {{0, 1, 2}, {0, 2, 1}, {1, 0, 2}, {1, 2, 0}, {2, 0, 1}, {2, 1, 0}};

    public int[] getRandomPermutation() {
        Random rnd = new Random();
        int nextInt = rnd.nextInt(permutation.length);
        return permutation[nextInt];
    }

}
