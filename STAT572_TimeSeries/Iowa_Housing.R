########################################################################
#                                                                      #
#                   STAT 572 - Seasonal Data                           #
#                   Iowa Housing Inventory                             #
#                        Yen Vo                                        #
#                                                                      #
########################################################################





library(RTseries)
setwd("C:/ISU Master Degree/Spring 2024/STAT 572/Individual Project")
getwd()



# Access data
house_inventory <- read.csv("Iowa_Housing_Inventory.csv")
head(house_inventory)
summary(house_inventory)

# convert house inventory data to time series data
df = house_inventory$MEDDAYONMARIA 

house_inventory.ts <- ts(as.vector(t(df)), start = c(2017,1), frequency = 12)
house_inventory.ts
plot(house_inventory.ts)

# convert data to tsd
house_inventory.tsd <- tsd(house_inventory.ts, data.title='Iowa Housing Inventory Starts 2017-2024',
                  response.units='# of Houses', time.units='Year')

# Plot tim series data
plot(house_inventory.tsd)
abline(h=mean(house_inventory.tsd),lwd=2,col=6)


################################################################################
#                                                                              #
#                     Model Identification                                     #
#                                                                              #
################################################################################

# This is the iden for Iowa housing data with no differencing and no 
# transformation

# ACF shows a clear seasonality pattern, the peak occurs at 12, as it observation
# 12 times period, the ACF is dying down at seasoanl lag, 12,24,36, and
# dying down slowly enough that may take a seasonal different
iden(house_inventory.tsd)

#Taking 1 regular different, still have an evidence of seasoanity,
# ACF is dying down slowly, as we go from 12 to 24, 36, this is a hint
# perhap we should take seasonal differencing here
iden(house_inventory.tsd, gamma=0, d=1)
iden(house_inventory.tsd, d=1)
iden(house_inventory.tsd, d=1, D = 1)


#taking 1 seasonal different
#PACF is dying down at 12,24,36, 
# seasonal cutting off for ACF
# Seasoanly dying down behavior in PACF, suggestion to use seasonal
# cut off term for moving average model
iden(house_inventory.tsd, gamma=0, D=1)
iden(house_inventory.tsd, D=1)
iden(house_inventory.tsd, gamma = 0)
# Both differencing
# rule of thumb, if you take a seasonal different, you should go with moving 
# average term
# The PACF may dying down at seasonal lag, and the ACFis cutting off after 
# one seasonal lag, suggest we should use moving average term--- may be
# moving average 2 for regular part


################################################################################
#                                                                              #
#                           Model Estimation                                   #
#                                                                              #
################################################################################


####### fit and compare some of models####

esti(house_inventory.tsd, model=model.pdq(d=1, q=1, D=1,Q=1),print.table = TRUE, y.range=c(20, 150))
esti(house_inventory.tsd, gamma = 0, model=model.pdq(d=1, q=2, D=1,Q=2),print.table = TRUE, y.range=c(20, 150))
esti(house_inventory.tsd,gamma = 0, model=model.pdq(d=1, q=1, D=1,Q=1),print.table = TRUE, y.range=c(20, 150))

esti(house_inventory.tsd,gamma = 0, model=model.pdq(p=1, d=1, q=1, P = 1, D=1,Q=1),print.table = TRUE, y.range=c(20, 150))

# Deviation in 2020 - covid, could do a further invstigation
esti(house_inventory.tsd, gamma=0, model=model.pdq(d=1, q=2, D=1, Q=1),print.table = TRUE, y.range=c(20, 150))
esti(house_inventory.tsd, gamma=0, model=model.pdq(d=1, q=1, D=1, Q=1),print.table = TRUE, y.range=c(20, 150))
# The prediction--- the effect of seasonality, because of the nature of the time
# series here,recall 1 regular different, if i want to see the next month,
# i look back and see the level same month one year ago,
# There is a couple deviation in the year 2020, the time series has a higher
# inventory than it has been predicted ---> covid, reduce mortgaget rate in that
# particular year

### summary table of model, no transformation,
# 1 first model--- 1 normal difference
# 2nd model----1 difference 


##
# Same - deviation in 2020
#Model SARIMA(1,0,1)(0,1,1)
esti(house_inventory.tsd, gamma=0, model=model.pdq(d=1, q=1, D=1, Q=1),print.table = TRUE, y.range=c(20, 150))


