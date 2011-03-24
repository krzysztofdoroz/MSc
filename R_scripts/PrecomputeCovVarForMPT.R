##################### 
# Author: krzysztof #
#####################

#
#loading data from files:
#
	kghm_2010 <- read.table("/home/krzysztof/MSc/data-source/kghm.data", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	tpsa_2010 <- read.table("/home/krzysztof/MSc/data-source/tpsa.data", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	kghm_2009 <- read.table("/home/krzysztof/MSc/data-source/kghm_2009", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	tpsa_2009 <- read.table("/home/krzysztof/MSc/data-source/tpsa_2009", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	wig20_2009 <- read.table("/home/krzysztof/MSc/data/wig20_2009", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	wig20_2010 <- read.table("/home/krzysztof/MSc/data/wig20_2010", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	pko_2010 <- read.table("/home/krzysztof/MSc/data-source/pko.data", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	pko_2009 <- read.table("/home/krzysztof/MSc/data-source/pko_2009", sep="", na.strings="NA", dec=".", strip.white=TRUE)

#
#merging data from 2009 and 2010
#calculating 
#

	kghm_all <- rbind(kghm_2009,kghm_2010)
	tpsa_all <- rbind(tpsa_2009,tpsa_2010)
	wig20_all <- rbind(wig20_2009,wig20_2010)
	pko_all <- rbind(pko_2009,pko_2010)
	
	total_size <- length(kghm_2010$V1)
	start_index <- length(kghm_2009$V1)
	
#	
#covariance of kghm_all and wig20_all, tpsa_all and wig20_all datasets: cov(kghm_all,wig20_all), cov(tpsa_all,wig20_all)
#variance of wig20_all: var(wig20_all)
#

	for( i in 1:total_size) {
		end_index <- start_index + i
		write(cov(kghm_all$V1[1:end_index],wig20_all$V1[1:end_index]), file="output/kghm-wig20_cov", append = TRUE)
		write(var(wig20_all$V1[1:end_index]), file="output/wig20_var", append = TRUE)
		write(cov(tpsa_all$V1[1:end_index],wig20_all$V1[1:end_index]), file="output/tpsa-wig20_cov", append = TRUE)
		write(cov(pko_all$V1[1:end_index],wig20_all$V1[1:end_index]), file="output/pko-wig20_cov", append = TRUE)
	}
	
