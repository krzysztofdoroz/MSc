##################### 
# Author: krzysztof #
#####################

#
#Entry points: SMA of last 20 days > SMA of last 40 days -> GO LONG
#			   today's stock price is max of last 20 days stock price -> GO LONG	
#
#Exit: 		   if losses on a single trade > 2.0% -> GO SHORT
#			   SMA of last 40 days > SMA of last 20 days -> GO SHORT	
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
	
	result = current_money_ammount
	
	for(i in 1:2){
		result = result + (trades[i,2] * data[index])
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
	current_money_ammount = 100.0
	initial_money_ammount = 100.0
	money_cap_per_single_trade = 0.5 
	#plot(tpsa_2010$V1,type="o", col="black")
	trades <- array(c(1:2,1:2), dim=c(2,2))
	initialize()
	
	## portfolio_value 
	
#
#lets seek some trends out there
#
print(stock_prices[,2])

solve <- function() {
	counter = 0
	for(i in start_index:end_index){
		#for each stock in portfolio:
		for(j in 1:ncol(stock_prices)){
			
			if (get_most_recent_trade_stock_price(j) < 0.98 * get_current_stock_price(stock_prices[,j],i)){
				go_short(stock_prices[,j],i,j)
			} else if (simple_moving_average(stock_prices[,j],i,20) < simple_moving_average(stock_prices[,j],i,40)){
				go_short(stock_prices[,j],i,j)
			} 
			
			if (simple_moving_average(stock_prices[,j],i,20) > simple_moving_average(stock_prices[,j],i,40)){
				go_long(stock_prices[,j],i,j)
			} else if (max_historical_price(stock_prices[,j], i, 20) < stock_prices[i,j] ){
				go_long(stock_prices[,j],i,j)
			} 
		}
	}
}

solve()

print(trades)
print(current_money_ammount)


