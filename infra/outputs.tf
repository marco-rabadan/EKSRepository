output "cluster_id" {
  description = "EKS cluster ID"
  value       = aws_eks_cluster.eks-cluster-services.id
}

output "cluster_endpoint" {
  description = "Endpoint for EKS control plane"
  value       = aws_eks_cluster.eks-cluster-services.endpoint
}

output "cluster_name" {
    description = "EKS cluster name"
    value = aws_eks_cluster.eks-cluster-services.name
}

output "cluster_ca_cert" {
    description = "EKS cluster name"
    value = aws_eks_cluster.eks-cluster-services.certificate_authority[0].data
}

output "cluster_kubectl" {
    description = "kbectl config"
    value = aws_eks_cluster.eks-cluster-services.identity
}

output "cluster" {
    description = "cluster"
    value = aws_eks_cluster.eks-cluster-services
}