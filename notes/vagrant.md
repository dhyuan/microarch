###0 About vagrant please [reference official site](https://www.vagrantup.com/docs/).



#####1) Create Base Host

###### Generate the vagrant project.

	export WORK_DIR=....
	cd $WORK_DIR/microarch/topology/mongo_sharding

	vagrant init

###### Download CentOS7 box.

	vagrant box add  geerlingguy/centos7


###### Use the centos7 as the base.

	Vagrant.configure("2") do |config|
	  config.vm.box = "geerlingguy/centos7"
	end

 
 

#####2) Start up host and access it

###### start up
	vagarant up
	
###### ssh in
	vagarant ssh
	
###### Destory host	
	vagrant destroy
	
###### Remove box
	vagrant box remove
	
		

#####3) Synced folders

  _The current project folder is mapped as the directory "/vagrant" in the guest host._

	
###### ssh in
	vagarant ssh
	



https://atlas.hashicorp.com/account/new



