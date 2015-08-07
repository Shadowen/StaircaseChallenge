# Staircase Challenge
A simple non-grid based [A* pathfinder](https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode).

## Problem Statement ([original](https://www.hackerrank.com/contests/frost-byte-final/challenges/stairway))
Rajarshee is on a treasure hunt with you inside a lonely palace. You two have came to know that there is a good possibility of existence of gold in the first floor. You two are in the ground floor. Both of you decide to go up. At the same time, you two discover that there are two parallel stairways **A** and **B** with **N** steps to go to the upper floor! While Rajarshee is busy in thinking what may be the use of two identical stairways, you suddenly discover a signboard:

"Better watch your step, fools! Each step you take cost you some, each time you switch stairways (`A` to `B` or `B` to `A`) it cost some more! You disobey and Death shall befall you."

The next moment both of you see each step has a cost written on it and they are to place money on each step accordingly before you visit next step and you can go **forward** from any step to another even if two belong to different stairways in which case it cost an additional **P** . You may also jump at most **K** steps i.e. maximum of ith step to `(i+K)`th step even if the two steps belong to different stairways.

Rajarshee still doesn't get discouraged by the obstacle, decides to go ahead. You agree too. Now, Rajarshee wants to save money as much as he can. In the same time, he being more like a dumb person has no idea how he can solve the money problem. He asks you, the intelligent programmer, for a solution. Fortunately, you have your laptop with you! So, you decide to write a code that will solve this problem and help Rajarshee in determining the exact minimum amount he need to spend in order to go up i.e. reach either `A[N-1]` or `B[N-1]`.

## Constraints

    1 ≤ T ≤ 10
    1 ≤ N ≤ 1000
    0 ≤ P ≤ 1000
    1 ≤ K ≤ N
    0 ≤ A[i], B[i] ≤ 10000

## Input Format

The first line in input is equal to T, the number of test cases. Then follows the description of `T` test cases. The first line in each test case has three integers N, the number of steps in both stairways, K, maximum jump length, P, penalty for crossing the stairs. On the second line of each test case there are `N` integers where ith integer represents penalty of step `A[i]`. On the third line of each test there are `N` integers where ith integer represents penalty of step `B[i]`.

## Output Format

For each test case, output a single line containing the minimum penalty you can accumulate on your path starting from `{ A[1] or B[1] }` and ending on `{ A[N] or B[N] }`.

### Sample Input

    1
    4 1 0 
    1 2 3 4
    1 2 3 4

### Sample Output

    10

### Explanation

Both the stairs are same and jump length is 1 , so you need to visit all steps on single stairway Min Path : `A[1]`, `A[2]`, `A[3]`, `A[4]`

Hence answer is 10 for first test case.
