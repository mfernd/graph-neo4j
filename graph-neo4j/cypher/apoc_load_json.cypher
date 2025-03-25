// relative to `/var/lib/neo4j/import/` in the Docker
CALL apoc.load.json("file:///data/test_data.json") YIELD value;
