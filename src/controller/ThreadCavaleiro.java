package controller;

import java.util.concurrent.Semaphore;

public class ThreadCavaleiro extends Thread {
	
	final int TAMANHO_CORREDOR = 2000;
	
	static boolean tochaSemDono = true;
	static boolean pedraSemDono = true;
	
	int id;
	Semaphore semaforo;
	Porta[] portas;
	int distPercorrida;
	int passoBonus;
	
	public ThreadCavaleiro(int id, Semaphore semaforo, Porta[] portas) {
		this.id = id;
		this.semaforo = semaforo;
		this.portas = portas;
		distPercorrida = 0;
		passoBonus = 0;
	}
	
	@Override
	public void run() {
		percorrerCorredor();
	}
	
	private void percorrerCorredor() {
		while(distPercorrida < TAMANHO_CORREDOR) {
			if (tochaSemDono && distPercorrida >= 500) {
				try {
					// Início da sessão crítica
					semaforo.acquire();
					tochaSemDono = false;
					passoBonus = 2;
					System.err.println(String.format("Cavaleiro %d pegou a tocha!", id));
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				} finally {
					semaforo.release();
					// Fim da sessão crítica
				}
			}
			
			if (pedraSemDono && passoBonus == 0 && distPercorrida >= 1500) {
				try {
					// Início da sessão crítica
					semaforo.acquire();
					pedraSemDono = false;
					passoBonus = 2;
					System.err.println(String.format("Cavaleiro %d pegou a pedra!", id));
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				} finally {
					semaforo.release();
					// Fim da sessão crítica
				}
			}
			
			int passo = (int) (Math.random() * (4 - 2 + 1) + 2 + passoBonus);
			distPercorrida += passo;
			System.out.println(String.format("Cavaleiro %d caminhou %d m!", id, passo));
			
			try {
				sleep(50);
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
		
		try {
			// Início da sessão crítica
			semaforo.acquire();
			
			boolean abriuPorta = false;
			do {
				int portaUsada = (int) (Math.random() * 4);
				if (!portas[portaUsada].aberta) {
					abriuPorta = true;
					portas[portaUsada].aberta = true;
					
					if (portas[portaUsada].conteudo.equals("saida")) {
						System.err.println(String.format("Cavaleiro %d encontrou a saída!", id));
					} else {
						System.err.println(String.format("Cavaleiro %d foi devorado por um monstro!", id));
					}
				}
			} while(!abriuPorta);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		} finally {
			semaforo.release();
			// Fim da sessão crítica
		}
	}
}
