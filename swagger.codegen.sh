
cd ressources/api/swagger/
java -jar swagger-codegen-cli-3.0.46.jar generate -i http://localhost:8080/api/v3/api-docs -l typescript-axios -o ../../../src/frontend/react-admin/src/generated
# java -jar swagger-codegen-cli-3.0.46.jar generate -i ../../../src/backend/spring-boot/target/swagger-ui/api-docs.yaml -l typescript-axios -o ../../../src/frontend/react-admin/src/generated

#Remplcer la sous chaine ./dist par ./
file="package.json"
search="./dist/"
replace="./"

cd ../../../src/frontend/react-admin/src/generated/
sed -i 's/"main": "\.\/dist\/index\.js"/"main": "\.\/index\.js"/g' $file
sed -i 's/"typings": "\.\/dist\/index\.d\.ts"/"typings": "\.\/index\.d\.ts"/g' $file

echo "./dist supprime avec succes"