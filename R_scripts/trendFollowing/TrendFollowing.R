##################### 
# Author: krzysztof #
#####################

#
#Entry points: SMA of last lower_number_of_days days > SMA of last higher_number_of_days days -> GO LONG
#			   today's stock price is max of last lower_number_of_days days stock price -> GO LONG	
#
#Exit: 		   if losses on a single trade > 2.0% -> GO SHORT
#			   SMA of last higher_number_of_days days > SMA of last lower_number_of_days days -> GO SHORT	
#

#
#SMA: calculates the average of data from data[index - howMany .. index - 1]
#
simple_moving_average <- function(data, index, howMany) {
	sum = 0.0
	
	for(i in 1:howMany){
		sum = sum + data[index - howMany + i]
	}

	sum / howMany
}

max_historical_price <- function(data, i, howMany) {
	max(data[(i-howMany):(i-1)])
}

go_long <- function(data, index, stockId){
	if(trades[stockId,1] == 0){
		print("TRADE IS POSSIBLE")
		
		#current price
		trades[stockId,1] <<- data[index] 
		#ammount
		trades[stockId,2] <<- (money_cap_per_single_trade * current_money_ammount) / data[index]
		current_money_ammount <<- current_money_ammount - (money_cap_per_single_trade * current_money_ammount)
	}
}

go_short <- function(data, index, stockId) {
	if(trades[stockId,1] > 0){
		print("GOING SHORT IS POSSIBLE")
		print("we bought for:")
		print(trades[stockId,1])
		print("we sell for:")
		print(data[index])
		#we are clearing
		trades[stockId,1] <<- 0
		current_money_ammount <<- current_money_ammount + (trades[stockId,2]* data[index])
		trades[stockId,2] <<- 0
	}
	
	#portfolio_value <<- cbind(portfolio_value,current_money_ammount)
	
}

get_most_recent_trade_stock_price <- function(stockID) {
	trades[stockID,1]	
}

get_current_portfolio_value <- function(data,index) {
	
	result <- current_money_ammount
	
	for(i in 1:2){
		result <- result + (trades[i,2] * data[index, i])
	}
	result
}

initialize <- function() {
	for(i in 1:2){
		for(j in 1:2){
			trades[i,j] <<- 0.0
		}	
	}	
}

get_current_stock_price <- function(data, index) {
	data[index]
}

#
#loading data from files:
#
	kghm_2010 <- read.table("/home/krzysztof/MSc/data-source/kghm.data", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	tpsa_2010 <- read.table("/home/krzysztof/MSc/data-source/tpsa.data", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	kghm_2009 <- read.table("/home/krzysztof/MSc/data-source/kghm_2009", sep="", na.strings="NA", dec=".", strip.white=TRUE)
	tpsa_2009 <- read.table("/home/krzysztof/MSc/data-source/tpsa_2009", sep="", na.strings="NA", dec=".", strip.white=TRUE)

#
#merging data sets
#
	kghm_all <- rbind(kghm_2009,kghm_2010)
	tpsa_all <- rbind(tpsa_2009,tpsa_2010)	
	stock_prices <- cbind(kghm_all,tpsa_all)
	colnames(stock_prices) <- c("V1","V2")
	#dim(stock_prices) <- c(length(kghm_all$V1), 2)

#
#calculate indices -> data dependant
#
	start_index = length(kghm_2009$V1) + 1
	end_index = length(kghm_all$V1)
	
#
#other variables initialization
#
	current_money_ammount <- 100.0
	initial_money_ammount <- 100.0
	money_cap_per_single_trade <- 0.5
	maximal_value_loss <- 0.98
	lower_number_of_days <- 10
	higher_number_of_days <- 20
	trades <- array(c(1:2,1:2), dim=c(2,2))
	initialize()
	
	portfolio_value <- array(c(1:length(kghm_2010$V1)), dim=c(length(kghm_2010$V1), 1))
	print(portfolio_value)
#
#lets seek some trends out there
#

solve <- function() {
	for(i in start_index:end_index){
		#for each stock in portfolio:
		for(j in 1:ncol(stock_prices)){
			
			if (get_most_recent_trade_stock_price(j) < maximal_value_loss * get_current_stock_price(stock_prices[,j],i)){
				go_short(stock_prices[,j],i,j)
			} else if (simple_moving_average(stock_prices[,j],i,lower_number_of_days) < simple_moving_average(stock_prices[,j],i,higher_number_of_days)){
				go_short(stock_prices[,j],i,j)
			} 
			
			if (simple_moving_average(stock_prices[,j],i,lower_number_of_days) > simple_moving_average(stock_prices[,j],i,higher_number_of_days)){
				go_long(stock_prices[,j],i,j)
			} else if (max_historical_price(stock_prices[,j], i, lower_number_of_days) < stock_prices[i,j] ){
				go_long(stock_prices[,j],i,j)
			} 
		}
		portfolio_value[i - start_index + 1] <<- get_current_portfolio_value(stock_prices, i)
	}
}

solve()

print(trades)
print(current_money_ammount)
print(portfolio_value)

plot(portfolio_value, type='o')
