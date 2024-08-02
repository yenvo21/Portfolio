library(RTseries)
################################################################################
#                              STAT 472/572                                    #
#                                Group 3                                       #
#                                                                              #
#            Kassandra Chino Gonzalez, Ryan Nagao, Yen Vo                      #
#                                                                              #
#               Annual Iowa Personal Income Analysis                           #
#                                                                              #
################################################################################


setwd("C:/ISU Master Degree/Spring 2024/STAT 572/Group Project_Nonseasonal")
getwd()

iowa_income<-read.csv("Annual_Personal_Income_for_State_of_Iowa.csv")
head(iowa_income)
summary(iowa_income$Value)

# Keep only variable with Personal income data
new_df<- subset(iowa_income,startsWith(Variable,"Personal income"))
new_df

# Convert income value to ts
income_value <- new_df$Value
summary(income_value)

# Data is collected annually, frequency = 1
income_value.ts <- ts(as.vector(t(income_value)), start = 1999, frequency = 1, end = 2020)

plot(income_value.ts)

# convert ts data to tsd
income_value.tsd <- tsd(income_value.ts,
                     data.title='Annual Iowa Personal Income',
                     response.units='in Millions of Dollars', time.units='Year')

plot(income_value.tsd)

##### Plot with the mean########################
plot(income_value.tsd)
abline(h=mean(income_value.tsd),lwd=2,col=6)


# Sign of AR(1) model since PACF is cutting off at lag 1
# However the series is nonstationary since ACF is dying down slowly

##### Identification############################
iden(income_value.tsd)

#Differencing, 1st different
iden(income_value.tsd, d = 1)

########Estimation#############################
# ignore scientific number
options(scipen = 100, digits = 4)
# Fit an esti command
# AR(1) Model
esti(income_value.tsd,model=model.pdq(p=1))
# AR(1,1,0)
esti(income_value.tsd,model=model.pdq(p=1,d =1))
# This model is looking good with lower standard error
esti(income_value.tsd, model=model.pdq(p=1),gamma=0)

#AR(2) Model------Forecast for AR(2) is looking good---QQ Plot is also
# better than other two models
esti(income_value.tsd,model=model.pdq(p=2),gamma=0)

# ARIMA(1,0,1) Model and Gamma = 0----Not looking good
esti(income_value.tsd, model=model.pdq(p=1,d=1,q=1),gamma=0)

## ARIMA(2,0,1) Model this is also another good model----- will check
# statistics summary to see ARIMA(2,0,1) or AR(2) is a better choice
esti(income_value.tsd, model=model.pdq(p=2,q=1),gamma=0)

#### MA(1) and MA(2) are testing model, not include in final report
#MA(1) Model------forecast  and residual plot are not looking good,
esti(income_value.tsd, model=model.pdq(q=1),gamma=0,print.table = TRUE)
# MA(2) Model
esti(income_value.tsd, model=model.pdq(q=2),gamma=0)

#####################Final Model######################
## After reviewing statistics summary and diagnotics #
## checking, our final model is ARIMA(2,0,1) with    #
## gamma = 0 #########################################
