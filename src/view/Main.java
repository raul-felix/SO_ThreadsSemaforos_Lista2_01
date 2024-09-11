package view;

import java.util.concurrent.Semaphore;

import controller.Porta;
import controller.ThreadCavaleiro;

public class Main {
	
	public static void main(String[] args) {
		
		int permissoes = 1;
		Semaphore semaforo = new Semaphore(permissoes);
		
		Porta[] portas = new Porta[4];
		int portaCerta = (int) (Math.random() * 4);
		
		for (int i = 0; i < portas.length; i++) {
			if (i == portaCerta) {
				portas[i] = new Porta("saida", false);
			} else {
				portas[i] = new Porta("monstro", false);
			}
		}
		
		for (int i = 1; i <= 4; i++) {
			Thread threadCavaleiro = new ThreadCavaleiro(i, semaforo, portas);
			threadCavaleiro.start();
		}
	}

}
