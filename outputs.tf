output "cluster_id" {
  description = "EKS cluster ID"
  value       = aws_eks_cluster.eks-cluster-test.id
}

output "cluster_endpoint" {
  description = "Endpoint for EKS control plane"
  value       = aws_eks_cluster.eks-cluster-test.endpoint
}

output "cluster_name" {
    description = "EKS cluster name"
    value = aws_eks_cluster.eks-cluster-test.name
}

output "cluster_ca_cert" {
    description = "EKS cluster name"
    value = aws_eks_cluster.eks-cluster-test.certificate_authority[0].data
}

output "cluster_kubectl" {
    description = "kbectl config"
    value = aws_eks_cluster.eks-cluster-test.identity
}

output "cluster" {
    description = "cluster"
    value = aws_eks_cluster.eks-cluster-test
}