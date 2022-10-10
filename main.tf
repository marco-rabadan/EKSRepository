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
  ami           = "ami-026b57f3c383c2eec"
  instance_type = "t2.micro"

  tags = {
    Name = "server_jenkins"
  }
}
