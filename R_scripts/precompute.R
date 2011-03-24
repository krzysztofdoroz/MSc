##################### 
# Author: krzysztof #
#####################

#
#loading data from files:
#
	kghm <- read.table("/home/krzysztof/MSc/data-source/kghm.data", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	tpsa <- read.table("/home/krzysztof/MSc/data-source/tpsa.data", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	kghm_2009 <- read.table("/home/krzysztof/MSc/data-source/kghm_2009", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	tpsa_2009 <- read.table("/home/krzysztof/MSc/data-source/tpsa_2009", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	pko <- read.table("/home/krzysztof/MSc/data-source/pko.data", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	pko_2009 <- read.table("/home/krzysztof/MSc/data-source/pko_2009", sep="", na.strings="NA", dec=".", strip.white=TRUE)
#
#standard deviation of kghm stocks 
#1st value is taken on 1st Jan 2009
#only values after 1st Jan 2010 are stored
#

	kghm_all <- rbind(kghm_2009,kghm)
	total_size <- length(kghm$V1)
	start_index <- length(kghm_2009$V1)

#
#standard deviation of tpsa stocks 
#1st value is taken on 1st Jan 2009
#only values after 1st Jan 2010 are stored
#datasets have the same size so there is no point
#in recomputing them
#	

	tpsa_all <- rbind(tpsa_2009,tpsa)

#
#same as before but for pko stock data
#	
	pko_all <- rbind(pko_2009, pko)

#	
#standard deviation of kghm_all and tpsa_all datasets
#correlation coefficient cor(kghm,tpsa)
#

	for( i in 1:total_size) {
		end_index <- start_index + i
		write(sd(kghm_all$V1[1:end_index]), file="output/kghm_standard_deviation", append = TRUE)
		write(sd(tpsa_all$V1[1:end_index]), file="output/tpsa_standard_deviation", append = TRUE)
		write(sd(pko_all$V1[1:end_index]) , file="output/pko_standard_deviation" , append = TRUE)
		write(cor(kghm_all$V1[1:end_index],tpsa_all$V1[1:end_index]), file="output/kghm-tpsa_correlation_coeff", append = TRUE)
		write(cor(kghm_all$V1[1:end_index],pko_all$V1[1:end_index]), file="output/kghm-pko_correlation_coeff", append = TRUE)
		write(cor(tpsa_all$V1[1:end_index],pko_all$V1[1:end_index]), file="output/tpsa-pko_correlation_coeff", append = TRUE)	
	}
	
