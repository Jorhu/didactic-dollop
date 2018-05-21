package org.git.joribiz.pmm.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Extensión inteligente de FragmentStatePageAdapter que cachea todos los fragments activos y
 * maneja sus ciclos de vida. Tomada de GitHub.
 */
public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    // Sparse array para saber qué fragmentos están almacenados en memoria
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * Registra el fragment cuando el ítem es instanciado
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Elimina del registro el fragmento cuando el ítem está inactivo.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    /**
     * Devuelve el fragment según su posición (si está instanciado).
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
