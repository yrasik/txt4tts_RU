# UTF-8
# Диалог. Начало пояснения автора
# 24.10.2017
# Yra
#####################################################


#Но, – сказала она
#делает, – весело отозвался владелец 
#([а-я][\,\?\!])(\s[-–]\s[а-я])=$1</voice><__AUTOR__ voice name:"__VOICE_AUTOR__" gender:"__GENDER_AUTOR__" age:"__AGE_AUTOR__">$2




([а-я][\,\?\!])(\s[-–]\s)(\bвздохнул\b|\bвоскликнул\b|\bсказал[а]?\b)=$1«tag1»$2$3


«tag1»=</voice><voice AUTOR>
#«tag1»=</voice><__AUTOR__ voice name:"__VOICE_AUTOR__" gender:"__GENDER_AUTOR__" age:"__AGE_AUTOR__">