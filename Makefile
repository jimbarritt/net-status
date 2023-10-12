# Not you will first need to upload your keys to the remote server
# https://unix.stackexchange.com/questions/36540/why-am-i-still-getting-a-password-prompt-with-ssh-with-public-key-authentication
# Make sure that on the server that the file permissions for the server .ssh directory are correct:
# chmod -R 700 ~/.ssh
#
# You need to create a file called ~/.sftpnetstatus containing:
# export SITE_HOSTNAME=<name of sftp host>
# export SITE_USERNAME=<name of sftp user>
#
# Not related but interesting: https://stackoverflow.com/questions/24742223/makefile-dependent-targets-based-on-current-target


include ~/.sftpnetstatus

upload-ssh-key:
	cat ~/.ssh/id_rsa.pub | ssh ${SITE_USERNAME}@${SITE_HOSTNAME} 'cat >> ~/.ssh/authorized_keys'

package:
	lein uberjar
	cp ops/run-server.sh target

list-network:
	arp -a

ssh:
	ssh ${SITE_USERNAME}@${SITE_HOSTNAME}

ssh-debug:
	ssh -v ${SITE_USERNAME}@${SITE_HOSTNAME}


publish: package
	$(info Going to sftp the files up to the server: username [${SITE_USERNAME}], hostname [${SITE_HOSTNAME}])
	ssh ${SITE_USERNAME}@${SITE_HOSTNAME} 'mkdir -p ~/net-status'
	scp ./target/net-status-0.1.0-SNAPSHOT-standalone.jar ${SITE_USERNAME}@${SITE_HOSTNAME}:~/net-status/
	scp ./target/run-server.sh ${SITE_USERNAME}@${SITE_HOSTNAME}:~/net-status/

