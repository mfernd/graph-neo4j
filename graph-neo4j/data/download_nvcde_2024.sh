#!/bin/bash

wget https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-2024.json.zip
unzip nvdcve-1.1-2024.json.zip -d data/
rm nvdcve-1.1-2024.json.zip
