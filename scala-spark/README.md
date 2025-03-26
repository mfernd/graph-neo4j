# scala-spark

## Prerequisites

- [minikube](https://github.com/kubernetes/minikube)
- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/)
- [sbt](https://github.com/sbt/sbt)
- [just](https://github.com/casey/just)
- [helmfile](https://github.com/helmfile/helmfile)
- [jq](https://github.com/jqlang/jq)

## How to run Spark jobs

Create the cluster and create the bucket on MinIO.

```bash
just init
```

When spark-operator is ready, you can run the spark jobs.

```bash
just run-simple-app run-users-test
```

You can then get the output by reading the logs of the jobs.
