// Get the count directly with the `CVE_data_numberOfCVEs` field in JSON
WITH "file:///data/nvdcve-1.1-2024.json" as url 
CALL apoc.load.json(url) YIELD value 
UNWIND value.CVE_data_numberOfCVEs as Cnt
RETURN Cnt;
// ="37388"

// Get the count with the function
WITH "file:///data/nvdcve-1.1-2024.json" as url 
CALL apoc.load.json(url) YIELD value 
UNWIND value.CVE_Items as cve
RETURN count(cve);
// =37388

// Create Nodes from JSON Files
CALL apoc.periodic.iterate("CALL apoc.load.json('file:///data/nvdcve-1.1-2024.json') YIELD value",
"UNWIND  value.CVE_Items AS data  \r\n"+
"UNWIND data.cve.references.reference_data AS references \r\n"+
"MERGE (cveItem:CVE {uid: apoc.create.uuid()}) \r\n"+
"ON CREATE SET cveItem.cveid = data.cve.CVE_data_meta.ID, cveItem.references = references.url",
 {batchSize:100, iterateList:true});
