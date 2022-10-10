terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }
}

provider "aws" {
  region  = "us-east-1"
  access_key = "<access_key>"
  secret_key = "<secret_key>"
}

resource "aws_instance" "app_server_jenkins" {
  instance_type = "t2.micro"

  tags = {
    Name = "app_jenkins"
  }
}
