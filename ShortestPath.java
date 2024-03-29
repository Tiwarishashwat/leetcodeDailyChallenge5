//BFS Algorithm
class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        //adj map
        HashMap<Integer,List<int[]>> adjMap = new HashMap<>();
        // fill the adj map
        for(int i[] : flights){
            //i[0] - from from -> [[to,price],...]
            //i[1] - to
            //i[2] - price
            if(!adjMap.containsKey(i[0])){
                adjMap.put(i[0],new ArrayList<>());
            }
            adjMap.get(i[0]).add(new int[]{i[1],i[2]});
        }
        int priceArray[] = new int[n];
        Arrays.fill(priceArray,Integer.MAX_VALUE);
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{src,0});
        int stops=0;
        while(!queue.isEmpty() && stops<=k){
            int len = queue.size(); // level wise traversal
            while(len>0){
                int temp[] = queue.poll();
                int node = temp[0];
                int price = temp[1];
                if(!adjMap.containsKey(node)){
                    len--;
                    continue;
                }
                for(int neighbours[] : adjMap.get(node)){
                    int toNode = neighbours[0];
                    int toNodePrice = neighbours[1];
                    if(price + toNodePrice >= priceArray[toNode]){
                        continue;
                    }
                    priceArray[toNode]=price + toNodePrice;
                    queue.offer(new int[]{toNode,priceArray[toNode]});
                }
                len--;
            }
            stops++; // stops will increase at each level
        }
        return (priceArray[dst]==Integer.MAX_VALUE?-1:priceArray[dst]);
    }
}


//BellmanFord Algorithm
class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // Distance from source to all other nodes.
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        // source will be at a disatnce of 0
        dist[src] = 0;

        // Run only K+1 times since we want shortest distance in K hops (bellman ford traverses n-1 edges, so that means according to our question, there will be n-1 stops, but we need only K, so limit it to K)
        for (int i = 0; i <= k; i++) {
            // Create a copy of dist vector.
            int[] temp = Arrays.copyOf(dist, n);
            for (int[] flight : flights) {
                if (dist[flight[0]] != Integer.MAX_VALUE) {
                    temp[flight[1]] = Math.min(temp[flight[1]], dist[flight[0]] + flight[2]);
                }
            }
            // Copy the temp vector into dist.
            dist = temp;
        }
        return dist[dst] == Integer.MAX_VALUE ? -1 : dist[dst];
    }
}

//Dijkstra Algorithm
class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int[] i : flights)
            adj.computeIfAbsent(i[0], value -> new ArrayList<>()).add(new int[] { i[1], i[2] });

        int[] stops = new int[n];
        Arrays.fill(stops, Integer.MAX_VALUE);
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        // {dist_from_src_node, node, number_of_stops_from_src_node}
        pq.offer(new int[] { 0, src, 0 });

        while (!pq.isEmpty()) {
            int[] temp = pq.poll();
            int dist = temp[0];
            int node = temp[1];
            int steps = temp[2];
            // We have already encountered a path with a lower cost and fewer stops,
            // or the number of stops exceeds the limit.
            if (steps > stops[node] || steps > k + 1)
                continue;
            stops[node] = steps;
            if (node == dst)
                return dist;
            if (!adj.containsKey(node))
                continue;
            for (int[] a : adj.get(node)) {
                pq.offer(new int[] { dist + a[1], a[0], steps + 1 });
            }
        }
        return -1;
    }
}
