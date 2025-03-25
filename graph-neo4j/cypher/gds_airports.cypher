// # First start by creating a GDS project
CALL gds.graph.project(
  'routes',
  'Airport',
  'HAS_ROUTE'
)
YIELD
  graphName AS graph, nodeProjection, nodeCount AS nodes, relationshipProjection, relationshipCount AS rels;
CALL gds.graph.project(
  'routes-weighted',
  'Airport',
  'HAS_ROUTE',
  {
      relationshipProperties: 'distance'
  }
) YIELD
  graphName, nodeProjection, nodeCount, relationshipProjection, relationshipCount;



// # QUERIES ----------------------------------------------------------------

// CENTRALITY - Compute the page rank on routes
CALL gds.pageRank.stream('routes')
YIELD nodeId, score
WITH gds.util.asNode(nodeId) AS n, score AS pageRank
RETURN n.iata AS iata, n.descr AS description, pageRank
ORDER BY pageRank DESC, iata ASC;

// CENTRALITY - Compute the page rank and write them in nodes
CALL gds.pageRank.write('routes',
    {
        writeProperty: 'pageRank'
    }
)
YIELD nodePropertiesWritten, ranIterations;
MATCH (a:Airport)
RETURN a.iata AS iata, a.descr AS description, a.pageRank AS pageRank
ORDER BY a.pageRank DESC, a.iata ASC;

// Community detection
CALL gds.louvain.stream('routes')
YIELD nodeId, communityId, intermediateCommunityIds
WITH gds.util.asNode(nodeId) AS n, communityId
RETURN
    SIZE(COLLECT(n)) AS numberOfAirports,
    COLLECT(DISTINCT n.city) AS cities,
    communityId;

// Similarity - similar airports
CALL gds.nodeSimilarity.stream('routes')
YIELD node1, node2, similarity
WITH gds.util.asNode(node1) AS n1, gds.util.asNode(node2) AS n2, similarity
RETURN
    n1.iata AS iata,
    n1.city AS city,
    COLLECT({iata:n2.iata, city:n2.city, similarityScore: similarity}) AS similarAirports
ORDER BY city LIMIT 20;

// Get the shortest route between Paris and Tokyo
MATCH p = SHORTEST 10 (a1:Airport)-[route:HAS_ROUTE]->(a2:Airport)
WHERE a1.city = "Paris" AND a2.city = "Tokyo"
RETURN a1.descr, a2.descr, route.distance, length(p) AS result
ORDER BY route.distance ASC;

// Get the shortest route and add stops between them, order by the shortest path
MATCH p = ALL SHORTEST (a1:Airport)-[routes:HAS_ROUTE]-+(a2:Airport)
WHERE a1.iata = "MEL" AND a2.iata = "DEN"
RETURN reduce(total = 0, r IN relationships(p) | total + r.distance) as total_dist, [n in nodes(p) | n.descr] AS stops
ORDER BY total_dist ASC;

// Same as before, but with Dijkstra algorithm
MATCH (source:Airport {iata: 'DEN'})
MATCH (target:Airport {iata: 'MEL'})
CALL gds.shortestPath.dijkstra.stream('routes-weighted', {
    sourceNode: source,
    targetNode: target,
    relationshipWeightProperty: 'distance'
})
YIELD index, sourceNode, targetNode, totalCost, nodeIds, costs, path
RETURN
    index,
    gds.util.asNode(sourceNode).iata AS sourceNodeName,
    gds.util.asNode(targetNode).iata AS targetNodeName,
    totalCost,
    [nodeId IN nodeIds | gds.util.asNode(nodeId).descr] AS nodeNames,
    costs,
    nodes(path) as path
ORDER BY costs ASC;
