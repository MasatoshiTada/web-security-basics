terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.43.0"
    }
  }
  backend "local" {
    path = "terraform.tfstate"
  }
  required_version = ">= 1.2.0"
}

provider "aws" {
  profile = "default"
  region  = "ap-northeast-1"
}
