# net-status

A simple command line app which just polls a url to check wether there is an internet connection. 

This can then be used to create an observation log of internet up-time. I made this because we were experiencing intermittent internet drops in our home broadband and I wanted to be able to monitor it.

The idea is to deploy to a raspberry pi.

See this for how to install as a service https://github.com/ixcode/tekdoc/blob/master/publisher/init-d-definition.sh

## Setting up the pi


https://www.raspberrypi.com/software/

https://www.tomshardware.com/reviews/raspberry-pi-headless-setup-how-to,6028.html

Use the setup utility and can configure the pi to be headless and ssh-able

Configuring Wifi:

sudo vim /etc/wpa_supplicant/wpa_supplicant.conf

Will tell you if the wifi is up

ifconfig wlan0

## Usage

FIXME

## License

Copyright © 2023 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
